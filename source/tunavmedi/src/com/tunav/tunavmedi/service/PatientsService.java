
package com.tunav.tunavmedi.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Criteria;
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
        OnSharedPreferenceChangeListener {

    public class SelfBinder extends Binder {
        public PatientsService getService() {
            return PatientsService.this;
        }
    }

    public static final String tag = "PatientsService";;

    // This is the object that receives interactions from clients. See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new SelfBinder();

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
                    mPatientsAdapter.updateDataSet(oPatient);
                }
            });
        }
    };
    private ArrayList<Patient> mPatientsCache = new ArrayList<Patient>();
    private PatientsAdapter mPatientsAdapter = null;
    private PatientsHelper mHelper = null;
    private ScheduledExecutorService mScheduler = null;
    private PendingIntent pendingPoller;
    private Future<?> mHelperFuture = null;
    private Integer RADIUS = 3;

    private void enableHelperyWatcher(boolean on) {
        boolean notnull = mHelperFuture != null;
        if (!on && notnull) {
            mHelperFuture.cancel(false);
        } else if (on && notnull) {
            if (mHelperFuture.isCancelled() || mBatteryWatcherFuture.isDone()) {
                mHelperFuture = mScheduler.submit(mHelper.getNotifyTask());
            }
        } else if (on && !notnull) {
            mHelperFuture = mScheduler.submit(mHelper.getNotifyTask());
        }
    }

    public PatientsAdapter getAdapter() {
        Log.v(tag, "getAdapter()");
        if (mPatientsAdapter == null) {
            mPatientsAdapter = new PatientsAdapter(this);
            mPatientsAdapter.updateDataSet(mHelper.pullPatients());
            PatientsAdapter.setRadius(RADIUS);
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
        if (batteryOk && !isCharging && mPatientsAdapter != null) {
            startLocationPolling(Criteria.ACCURACY_HIGH, Criteria.POWER_HIGH);
        } else {
            stopLocationPolling();
        }

        if (batteryOk && isConnected) {
            enableHelperyWatcher(true);
        } else {
            enableHelperyWatcher(false);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(tag, "onCreate()");
        mScheduler = Executors.newScheduledThreadPool(corePoolSize);

        mHelper = new PatientsHelper(this);
        mHelper.addObserver(mHelperObserver);
        mHelperFuture = mScheduler.submit(mHelper.getNotifyTask());

        boolean batteryOk = BatteryReceiver.getBatteryOk(this);
        boolean isCharging = ChargingReceiver.isCharging(this);
        boolean noConnection = NetworkReceiver.isConnected(this);

        onConfigure(batteryOk, isCharging, noConnection);

    }

    @Override
    public void onDestroy() {
        Log.v(tag, "onDestroy()");

        mHelper.deleteObserver(mHelperObserver);
        mScheduler.shutdown();
        stopLocationPolling();
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
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
        Log.v(tag, "setupLocationPolling()");

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

        if (pendingPoller != null) {
            pendingPoller.cancel();
        }
        pendingPoller = PendingIntent.getBroadcast(this, 0, poller, 0);

        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                1000 * 60 * 3, pendingPoller);
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

    protected void updatePatients(Patient updatedPatient) {
        Log.v(tag, "updatePatients()");
        // FIXME
        mPatientsCache.add(updatedPatient);
        if (mHelper.pushPatients(mPatientsCache) == mPatientsCache.size()) {
            mPatientsCache.clear();
        }
    }
}
