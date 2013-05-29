
package com.tunav.tunavmedi.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.commonsware.cwac.locpoll.LocationPoller;
import com.tunav.tunavmedi.adapter.PatientsAdapter;
import com.tunav.tunavmedi.dal.sqlite.helper.PatientsHelper;
import com.tunav.tunavmedi.datatype.Patient;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class PatientsService extends Service {

    public class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO
            Log.v(tag, "batteryStatus.onReceive()");
            String action = intent.getAction();
            Log.i(tag, action);
            if (action.equals(Intent.ACTION_BATTERY_LOW)) {
                batteryOkay = false;
            } else if (action.equals(Intent.ACTION_BATTERY_OKAY)) {
                batteryOkay = true;
            }
            onConfigure();
        }
    }

    public class ChargingReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(tag, "ChargingReceiver.onReceive()");
            String action = intent.getAction();
            Log.i(tag, action);
            if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
                isCharging = true;
            } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                isCharging = false;
            }
            onConfigure();
        }

    }

    public class LocationReceiver extends BroadcastReceiver {
        public static final String tag = "LocationReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(tag, "onReceive()");

            Location loc = (Location) intent.getExtras().get(LocationPoller.EXTRA_LOCATION);
            String msg;

            if (loc == null) {
                msg = intent.getStringExtra(LocationPoller.EXTRA_ERROR);
                Log.e(tag, msg);
                Location lastKnown = (Location) intent.getExtras().get(
                        LocationPoller.EXTRA_LASTKNOWN);
                if (lastKnown == null) {
                    Log.e(tag, "LastLocation Unknown");
                } else {
                    PatientsAdapter.setLocation(lastKnown);
                    Log.i(tag, lastKnown.toString());
                }
            } else {
                PatientsAdapter.setLocation(loc);
                Log.i(tag, loc.toString());
            }
        }
    };

    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

        }

    }

    public class SelfBinder extends Binder {
        public PatientsService getService() {
            return PatientsService.this;
        }
    }

    public static final String tag = "PatientsService";
    final Integer BATTERY_LEVEL_LOW = 15;
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
    private ArrayList<Patient> mPatientsQueue = new ArrayList<Patient>();
    private PatientsAdapter mPatientsAdapter = null;
    private LocationManager locationManager = null;
    private AlarmManager alarm = null;
    private PatientsHelper mHelper = null;
    private ScheduledExecutorService mScheduler = null;
    private static PendingIntent pendingPoller = null;
    private Boolean isCharging = null;
    private Boolean batteryOkay = null;
    private Boolean isConnected = true; // TESTING PURPOSE ONLY
    private Future<?> mBatteryWatcherFuture = null;

    private Future<?> mHelperFuture = null;

    private Integer RADIUS = 3;

    private void enableHelperyWatcher(boolean on) {
        boolean notnull = mHelperFuture != null;
        if (!on && notnull) {
            mHelperFuture.cancel(false);
        } else if (on && notnull) {
            if (mHelperFuture.isCancelled() || mBatteryWatcherFuture.isDone()) {
                mHelperFuture = mScheduler.submit(mHelper.getNotificationRunnable());
            }
        } else if (on && !notnull) {
            mHelperFuture = mScheduler.submit(mHelper.getNotificationRunnable());
        }
    }

    private void enableLocationPolling(boolean on) {
        on = true;// TESTING PURPOSE ONLY
        Log.v(tag, "enableLocationPolling()");
        Log.i(tag, "enableLocationPolling: " + (on ? "true" : "false"));
        if (pendingPoller != null) {
            alarm.cancel(pendingPoller);
        }
        if (on) {
            setupPendingPoller(Criteria.ACCURACY_FINE, Criteria.POWER_HIGH);
            alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(),
                    1000 * 60 * 3, pendingPoller);
        }
    }

    public PatientsAdapter getAdapter() {
        Log.v(tag, "getAdapter()");
        if (mPatientsAdapter == null) {
            mPatientsAdapter = new PatientsAdapter(this);
            mPatientsAdapter.updateDataSet(mHelper.pullPatients());
            mPatientsAdapter.setRadius(RADIUS);
        }
        return mPatientsAdapter;
    }

    private float getBatteryLevel() {
        Log.v(tag, "getBatteryLevel()");

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return (level / (float) scale);
    }

    private boolean isCharging() {
        Log.v(tag, "isCharging()");

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        return isCharging;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(tag, "onBind()");
        return mBinder;
    }

    private void onConfigure() {
        Log.v(tag, "onConfigure()");
        if (batteryOkay && !isCharging && mPatientsAdapter != null) {
            enableLocationPolling(true);
        } else {
            enableLocationPolling(false);
        }

        if (batteryOkay && isConnected) {
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
        mHelperFuture = mScheduler.submit(mHelper.getNotificationRunnable());

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        IntentFilter batteryFilter = new IntentFilter();
        batteryFilter.addAction("android.intent.action.BATTERY_LOW");
        batteryFilter.addAction("android.intent.action.BATTERY_OKAY");

        registerReceiver(new BatteryReceiver(), batteryFilter);

        IntentFilter chargingFilter = new IntentFilter();
        chargingFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        chargingFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");

        registerReceiver(new ChargingReceiver(), chargingFilter);

        isCharging = isCharging();
        Log.i(tag, "isCharging: " + isCharging.toString());

        if (getBatteryLevel() <= BATTERY_LEVEL_LOW) {
            batteryOkay = false;
        } else {
            batteryOkay = true;
        }
        Log.i(tag, "batteryOkey: " + batteryOkay.toString());

        onConfigure();
    }

    @Override
    public void onDestroy() {
        mHelper.deleteObserver(mHelperObserver);
        mScheduler.shutdown();
        enableLocationPolling(false);
        unregisterReceiver(new LocationReceiver());
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("PresenterService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    private void setupPendingPoller(int accuracy, int power) {
        Log.v(tag, "setupLocationPolling()");

        Criteria criteria = new Criteria();
        criteria.setAccuracy(accuracy);
        criteria.setPowerRequirement(power);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);

        String bestProvider = locationManager.getBestProvider(criteria, true);
        Log.i(tag, "BestProvider: " + bestProvider);

        // Intent poller = new Intent(this, LocationPoller.class);
        Intent poller = new Intent("LOCATION_POLLER");
        poller.setClass(this, LocationPoller.class);
        poller.putExtra(LocationPoller.EXTRA_INTENT, new Intent(this, LocationReceiver.class));
        poller.putExtra(LocationPoller.EXTRA_PROVIDER, bestProvider);
        pendingPoller = PendingIntent.getBroadcast(this, 0, poller, 0);
    }

    protected void updatePatients(Patient updatedPatient) {
        Log.v(tag, "updatePatients()");
        mPatientsQueue.add(updatedPatient);
        if (mHelper.pushPatients(mPatientsQueue) == mPatientsQueue.size()) {
            mPatientsQueue.clear();
        }
    }
}
