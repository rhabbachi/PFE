
package com.tunav.tunavmedi.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.tunav.tunavmedi.broadcastreceiver.BatteryReceiver;
import com.tunav.tunavmedi.broadcastreceiver.ChargingReceiver;
import com.tunav.tunavmedi.broadcastreceiver.NetworkReceiver;
import com.tunav.tunavmedi.dal.sqlite.helper.PatientsHelper;
import com.tunav.tunavmedi.datatype.Patient;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class PatientsService extends Service implements
        OnSharedPreferenceChangeListener,
        LocationListener {

    public interface PatientsListener {
        public void setLocation(Location location);

        public void updateDataSet(ArrayList<Patient> newPatients);
    }

    public class SelfBinder extends Binder {
        public PatientsService getService() {
            return PatientsService.this;
        }
    }

    public static final String tag = "PatientsService";

    // This is the object that receives interactions from clients. See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new SelfBinder();

    private final Integer corePoolSize = 2;;

    private Observer mHelperObserver = new Observer() {
        @Override
        public void update(Observable observable, Object o) {
            Log.v(tag + ".mHelperObserver", "update()");
            @SuppressWarnings("unchecked")
            final ArrayList<Patient> oPatient = (ArrayList<Patient>) o;
            mScheduler.submit(new Runnable() {
                @Override
                public void run() {
                    syncPatients(oPatient);
                }
            });
        }
    };

    private ArrayList<Patient> mPatientsCache = new ArrayList<Patient>();
    private ArrayList<PatientsListener> mPatientsListeners = new ArrayList<PatientsService.PatientsListener>();
    private Location mLocationCache;
    private PatientsHelper mHelper = null;
    private ScheduledExecutorService mScheduler = null;
    private Future<?> mPatientsFuture = null;
    private Future<?> mLocationFuture = null;

    public void addPatientsListener(PatientsListener listener) {
        if (listener != null && !mPatientsListeners.contains(listener)) {
            mPatientsListeners.add(listener);
        }
        updateListeners();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(tag, "onBind()");
        return mBinder;
    }

    private void onConfigure(boolean batteryOk, boolean isCharging, boolean isConnected) {
        Log.v(tag, "onConfigure()");
        // DEBUG ONLY
        batteryOk = true;
        isCharging = false;
        isConnected = true;

        if (batteryOk && !isCharging) {
            startLocationPolling(Criteria.ACCURACY_FINE, Criteria.POWER_HIGH, 1000 * 60 * 15, 30);
        } else {
            stopLocationPolling();
        }

        if (batteryOk && isConnected) {
            startPatientNotification();
        } else {
            stopPatientsNotification();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(tag, "onCreate()");

        mScheduler = Executors.newScheduledThreadPool(corePoolSize);

        mHelper = new PatientsHelper(this);
        mHelper.addObserver(mHelperObserver);
        syncPatients(mHelper.pullPatients());

        boolean batteryOk = BatteryReceiver.getBatteryOk(this);
        boolean isCharging = ChargingReceiver.isCharging(this);
        boolean noConnection = NetworkReceiver.isConnected(this);

        onConfigure(batteryOk, isCharging, noConnection);
    }

    @Override
    public void onDestroy() {
        Log.v(tag, "onDestroy()");

        mHelper.deleteObserver(mHelperObserver);

        stopLocationPolling();
        stopPatientsNotification();

        mScheduler.shutdownNow();

        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(tag, "onLocationChanged()");
        Log.i(tag, location.toString());

        mLocationCache = new Location(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.v(tag, "onProviderDisabled()");
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.v(tag, "onProviderEnabled()");
        // TODO Auto-generated method stub

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.v(tag, "onSharedPreferenceChanged()");
        // TODO Auto-generated method stub
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("PresenterService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.v(tag, "onStatusChanged()");
        // TODO Auto-generated method stub

    }

    public void removePatientsListener(PatientsListener listener) {
        mPatientsListeners.remove(listener);
    }

    private void startLocationPolling(int accuracy, int power, long minTime, float minDistance) {
        Log.v(tag, "startLocationPolling()");

        stopLocationPolling();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(accuracy);
        criteria.setPowerRequirement(power);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);

        String bestProvider = locationManager.getBestProvider(criteria,
                true);
        locationManager.requestLocationUpdates(bestProvider, minTime, minDistance, this);
        Log.i(tag, "Location Updates Requested!" + "\nProvider: " + bestProvider + "\nminTime: "
                + minTime + "\n minDistance: " + minDistance);
    }

    private void startPatientNotification() {
        Log.v(tag, "startPatientNotification()");
        if (mPatientsFuture != null) {
            mPatientsFuture.cancel(true);
            mPatientsFuture = null;
        }

        mPatientsFuture = mScheduler.submit(mHelper.getNotifyTask());
    }

    private void stopLocationPolling() {
        Log.v(tag, "stopLocationPolling()");

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
    }

    private void stopPatientsNotification() {
        Log.v(tag, "stopPatientsNotification()");
        if (mPatientsFuture != null) {
            mPatientsFuture.cancel(true);
            mPatientsFuture = null;
        }
    }

    public void syncPatient(Patient updatedPatient) {
        Log.v(tag, "syncPatient()");
        for (Patient patient : mPatientsCache) {
            if (patient.getId() == updatedPatient.getId()) {
                int index = mPatientsCache.indexOf(patient);
                mPatientsCache.set(index, updatedPatient);
                break;
            }
        }
        if (mHelper.pushPatients(mPatientsCache) > 0) {
            updateListeners();
        } else {
            Toast.makeText(this, "Probleme while updating from server!", Toast.LENGTH_LONG).show();
        }
    }

    public void syncPatients(ArrayList<Patient> newPatients) {
        Log.d(tag, "syncPatients()");
        mPatientsCache.clear();
        mPatientsCache.addAll(newPatients);
        updateListeners();
    }

    public void updateListeners() {
        Log.v(tag, "updateListeners()");
        for (PatientsListener listener : mPatientsListeners) {
            if (mLocationCache != null) {
                listener.setLocation(mLocationCache);
            }
            listener.updateDataSet(mPatientsCache);
        }
    }
}
