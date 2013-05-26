
package com.tunav.tunavmedi.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import com.commonsware.cwac.locpoll.LocationPoller;
import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.adapter.TasksAdapter;
import com.tunav.tunavmedi.broadcastreceiver.LocationMonitor;
import com.tunav.tunavmedi.datatype.Task;
import com.tunav.tunavmedi.demo.sqlite.helper.TasksHelper;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TasksService extends Service {

    public static final String tag = "TasksService";
    final Integer BATTERY_LEVEL_LOW = 15;
    // This is the object that receives interactions from clients. See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new SelfBinder();
    private final Integer corePoolSize = 2;
    private final Integer BATTERY_LEVEL_NOTMUCH = 40;
    private Observer observer = new Observer() {
        @Override
        public void update(Observable observable, Object o) {
            Log.v(tag, "update()");
            final ArrayList<Task> castedObject = (ArrayList<Task>) o;
            mScheduler.submit(new Runnable() {
                @Override
                public void run() {

                    mTasksAdapter.updateDataSet(castedObject);
                }
            });
        }
    };
    private ArrayList<Task> mTasksQueue = new ArrayList<Task>();
    private TasksAdapter mTasksAdapter = null;
    private LocationManager locationManager = null;
    private AlarmManager alarm = null;
    private TasksHelper mHelper = null;
    private ScheduledExecutorService mScheduler = null;
    private PendingIntent pendingLocator = null;
    private Boolean isCharging = null;
    private Boolean batteryOkay = null;
    private OnSharedPreferenceChangeListener statusListener;
    private Future<?> mBatteryWatcherFuture = null;
    private Runnable mBatteryWatcher = new Runnable() {

        @Override
        public void run() {
            Log.v(tag, "BatteryWatcher");
            onConfigure();
        }
    };
    private Future<?> mHelperWatcherFuture = null;

    private void enableLocationPulling() {

    }

    public TasksAdapter getAdapter() {
        mTasksAdapter = new TasksAdapter(this);
        mTasksAdapter.updateDataSet(mHelper.pullTasks());
        return mTasksAdapter;
    }

    private float getBatteryLevel() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return (level / (float) scale);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void onConfigure() {
        if (batteryOkay) {
            float lvl = getBatteryLevel();
            // selfcheck in case of
            if (lvl <= BATTERY_LEVEL_LOW) {
                batteryOkay = false;
                onConfigure();
                return;
            }
            toggleBatteryWatcher(true);
            if (BATTERY_LEVEL_NOTMUCH >= lvl && lvl >= BATTERY_LEVEL_LOW) {
                setupLocationPolling(Criteria.ACCURACY_COARSE, Criteria.POWER_LOW);
                toggleLocationPolling(true);
            } else if (lvl > BATTERY_LEVEL_NOTMUCH) {
                setupLocationPolling(Criteria.ACCURACY_FINE, Criteria.POWER_HIGH);
                toggleLocationPolling(true);
            }
        } else {
            toggleLocationPolling(false);
            toggleBatteryWatcher(false);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(tag, "onCreate()");
        mScheduler = Executors.newScheduledThreadPool(corePoolSize);

        mHelper = new TasksHelper(this);
        mHelper.addObserver(observer);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        setupSharedPref();
        onConfigure();
    }

    @Override
    public void onDestroy() {
        mHelper.deleteObserver(observer);
        mScheduler.shutdown();
        toggleLocationPolling(false);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("PresenterService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    private void setupLocationPolling(int accuracy, int power) {
        // TODO power management
        Criteria criteria = new Criteria();
        criteria.setAccuracy(accuracy);
        criteria.setPowerRequirement(power);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);

        String bestProvider = locationManager.getBestProvider(criteria, true);

        Intent poller = new Intent(this, LocationPoller.class);
        poller.putExtra(LocationPoller.EXTRA_INTENT, new Intent(this,
                LocationMonitor.class));
        poller.putExtra(LocationPoller.EXTRA_PROVIDER, bestProvider);
        pendingLocator = PendingIntent.getBroadcast(this, 0, poller, 0);
    }

    private void setupSharedPref() {
        // Status shared prefs
        statusListener = new OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                String keyIsCharging = getResources().getString(
                        R.string.sp_status_key_is_charging);
                String keyBatteryOkay = getResources().getString(
                        R.string.sp_status_key_battery_okey);
                if (key.equals(keyIsCharging)) {
                    isCharging = sharedPreferences.getBoolean(key, isCharging);
                } else if (key.equals(keyBatteryOkay)) {
                    batteryOkay = sharedPreferences.getBoolean(key, batteryOkay);
                }
            }
        };

        String spStatus = getResources().getString(R.string.sp_status_name);
        SharedPreferences mSharedPreferences = getSharedPreferences(spStatus, Context.MODE_PRIVATE);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(statusListener);
        // Hardcoded 15% low battery level
        batteryOkay = getBatteryLevel() > BATTERY_LEVEL_LOW ? true : false;
    }

    protected void updateTasks(Task updatedTask) {
        mTasksQueue.add(updatedTask);
        if (mHelper.pushTask(mTasksQueue) == mTasksQueue.size()) {
            mTasksQueue.clear();
        }
    }

    private void toggleBatteryWatcher(boolean on) {
        boolean notnull = mBatteryWatcherFuture != null;
        if (!on && notnull) {
            mBatteryWatcherFuture.cancel(false);
        } else if (on && notnull) {
            if (mBatteryWatcherFuture.isCancelled() || mBatteryWatcherFuture.isDone()) {
                mBatteryWatcherFuture = mScheduler.scheduleAtFixedRate(mBatteryWatcher, 30, 30, TimeUnit.MINUTES);
            }
        } else if (on && !notnull) {
            mBatteryWatcherFuture = mScheduler.scheduleAtFixedRate(mBatteryWatcher, 30, 30, TimeUnit.MINUTES);
        }
    }

    private void toggleHelperyWatcher(boolean on) {
        boolean notnull = mHelperWatcherFuture != null;
        if (!on && notnull) {
            mHelperWatcherFuture.cancel(false);
        } else if (on && notnull) {
            if (mHelperWatcherFuture.isCancelled() || mBatteryWatcherFuture.isDone()) {
                mHelperWatcherFuture = mScheduler.submit(mHelper.getNotificationRunnable());
            }
        } else if (on && !notnull) {
            mHelperWatcherFuture = mScheduler.submit(mHelper.getNotificationRunnable());
        }
    }

    private void toggleLocationPolling(boolean on) {
        boolean configured = pendingLocator != null;
        if (on && configured) {
            alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(),
                    1000 * 60 * 5, pendingLocator);
        } else if (!on && configured) {
            alarm.cancel(pendingLocator);
        }
    }

    public class SelfBinder extends Binder {
        public TasksService getService() {
            return TasksService.this;
        }
    }
}
