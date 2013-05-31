
package com.tunav.tunavmedi.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.commonsware.cwac.locpoll.LocationPoller;
import com.tunav.tunavmedi.adapter.PatientsAdapter;
import com.tunav.tunavmedi.broadcastreceiver.BatteryReceiver;
import com.tunav.tunavmedi.broadcastreceiver.ChargingReceiver;
import com.tunav.tunavmedi.broadcastreceiver.LocationReceiver;
import com.tunav.tunavmedi.broadcastreceiver.LocationReceiver.OnNewLocationListener;
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
        OnNewLocationListener {
    public class SelfBinder extends Binder {
        public PatientsService getService() {
            return PatientsService.this;
        }
    }

    public static final String tag = "PatientsService";

    // This is the object that receives interactions from clients. See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new SelfBinder();;

    private final Integer corePoolSize = 2;

    private Observer mHelperObserver = new Observer() {
        @Override
        public void update(Observable observable, Object o) {
            Log.v(tag + ".mHelperObserver", "update()");
            @SuppressWarnings("unchecked")
            final ArrayList<Patient> oPatient = (ArrayList<Patient>) o;
            mScheduler.submit(new Runnable() {
                @Override
                public void run() {
                    syncPatients(oPatient, true);
                }
            });
        }
    };
    private ArrayList<Patient> mPatientsCache = new ArrayList<Patient>();

    private Location mLocationCache = null;
    private PatientsAdapter mPatientsAdapter = null;
    private PatientsHelper mHelper = null;
    private ScheduledExecutorService mScheduler = null;
    private PendingIntent pendingPoller;
    private Future<?> mPatientsFuture = null;
    private Integer RADIUS = 3;

    public PatientsAdapter getAdapter() {
        Log.v(tag, "getAdapter()");
        if (mPatientsAdapter == null) {
            mPatientsAdapter = new PatientsAdapter(this);
            mPatientsAdapter.updateDataSet(mPatientsCache);
            mPatientsAdapter.setRadius(RADIUS);
        }
        return mPatientsAdapter;
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
        // DEBUG
        if (batteryOk && !isCharging) {
            startLocationPolling(Criteria.ACCURACY_FINE, Criteria.POWER_HIGH);
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

        LocationReceiver.setOnNewLocationListener(this);

        mScheduler = Executors.newScheduledThreadPool(corePoolSize);

        mHelper = new PatientsHelper(this);
        mHelper.addObserver(mHelperObserver);
        syncPatients(mHelper.pullPatients(), true);

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

        LocationReceiver.clearOnNewLocationListener(this);

        if (mPatientsAdapter != null) {
            mPatientsAdapter.notifyDataSetInvalidated();
            mPatientsAdapter = null;
        }

        mScheduler.shutdownNow();

        super.onDestroy();
    }

    @Override
    public void onNewLocationReceived(Location location) {
        Log.v(tag, "onNewLocationReceived()");
        mLocationCache = new Location(location);
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

    private void startLocationPolling(int accuracy, int power) {
        Log.v(tag, "startLocationPolling()");

        stopLocationPolling();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(accuracy);
        criteria.setPowerRequirement(power);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);

        String bestProvider = locationManager.getBestProvider(criteria, true);
        Log.i(tag, "BestProvider: " + bestProvider);

        Intent poller = new Intent("LOCATION_POLLER");
        poller.setClass(this, LocationPoller.class);
        poller.putExtra(LocationPoller.EXTRA_INTENT, new Intent(this, LocationReceiver.class));
        poller.putExtra(LocationPoller.EXTRA_PROVIDER, bestProvider);

        pendingPoller = PendingIntent.getBroadcast(this, 0, poller, 0);

        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                1000 * 60 * 3, pendingPoller);

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
        if (pendingPoller != null) {
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarm.cancel(pendingPoller);
            pendingPoller.cancel();
            pendingPoller = null;
        }
    }

    private void stopPatientsNotification() {
        Log.v(tag, "stopPatientsNotification()");
        if (mPatientsFuture != null) {
            mPatientsFuture.cancel(true);
            mPatientsFuture = null;
        }
    }

    public void syncPatient(Patient newPatient) {
        Log.v(tag, "syncPatient()");
        ArrayList<Patient> newPatients = new ArrayList<Patient>();
        newPatients.add(newPatient);
        syncPatients(newPatients, false);
    }

    public void syncPatients(ArrayList<Patient> newPatients, boolean override) {
        Log.d(tag, "syncPatients()");
        if (override) {
            mPatientsCache.clear();
            mPatientsCache.addAll(newPatients);
        } else {
            // TODO
        }

        if (mPatientsAdapter != null) {
            mPatientsAdapter.updateDataSet(mPatientsCache);
        }
    }
}
