
package com.tunav.tunavmedi.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonsware.cwac.locpoll.LocationPoller;
import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.abstraction.TasksHandler.OnTasksChangedListener;
import com.tunav.tunavmedi.broadcastreceiver.LocationMonitor;
import com.tunav.tunavmedi.datatype.Task;
import com.tunav.tunavmedi.demo.sqlite.helper.TasksHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TasksAdapter extends BaseAdapter {
    public enum Profile {
        BATTERY_LOW, NETWORK_DISABLE, BATTERY_OKAY, NETWORK_ENABLE;
    }

    static class TaskViewHolder {
        TextView task_title;
        TextView task_timer;
        ImageView task_image;
    }

    private static final String tag = "TasksAdapter";
    private volatile ArrayList<Task> mAllTasks = new ArrayList<Task>();
    private volatile ArrayList<Task> mDoneLessTasks = new ArrayList<Task>();
    private TasksHelper mHelper = null;
    private Context mContext = null;
    private final LayoutInflater mInflater;
    private Boolean showDone = null;
    private Boolean sortLocation = null;
    private SharedPreferences mSharedPreferences = null;
    private Location currentLocation = null;
    private Integer mRadius = null;

    private OnSharedPreferenceChangeListener tasklistListener;
    private OnSharedPreferenceChangeListener statusListener;

    private AlarmManager alarm = null;
    private LocationManager locationManager = null;
    private Runnable mSyncTasks = new Runnable() {
        private static final String tag = "TaskUpdater";

        @Override
        public void run() {
            Log.i(tag, "Syncing Tasks");
            ArrayList<Task> freshTasks = mHelper.pullTasks();
            updateDataSet(freshTasks);
            notifyDataSetChanged();
        }
    };
    private PendingIntent pendingPoller = null;
    private Boolean isCharging = null;
    private Boolean batteryOkey = null;
    private Thread mTasksMonitor = null;
    private OnTasksChangedListener mTasksListener = new OnTasksChangedListener() {
        private static final String tag = "OnTasksChangedListener";

        @Override
        public void onTasksChanged() {
            Log.v(tag, "onTasksChanged()");
            Thread worker = new Thread(mSyncTasks);
            worker.start();
        }
    };
    private ScheduledExecutorService mScheduler = null;
    private final Integer corePoolSize = 2;
    private final Integer MINUT = 1000 * 60;
    private Runnable refreshList = new Runnable() {
        private static final String tag = "refreshList";

        @Override
        public void run() {
            try {

                if (SystemClock.currentThreadTimeMillis() - mLastRefresh > 5 * MINUT) {
                    notifyDataSetChanged();
                    Log.v(tag, "refreshed!");
                } else {
                    Log.v(tag, "list too new!");
                }

            } catch (Exception tr) {
                Log.d(tag, "Exception!", tr);
            }
        }
    };
    private long mLastRefresh;

    public TasksAdapter(Context context) {
        Log.v(tag, "TasksAdapter()");
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mScheduler = Executors.newScheduledThreadPool(corePoolSize);

        mHelper = new TasksHelper(mContext);
        mHelper.addOnTasksChangedListener(mTasksListener);
        mScheduler.execute(mHelper.getNotificationRunnable());
        mScheduler.execute(mSyncTasks);

        locationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);
        alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        setupSharedPreference();
        setLocationPolling();

        mScheduler.scheduleWithFixedDelay(refreshList, 2, 2, TimeUnit.MINUTES);
    }

    private Comparator<Task> generateComparator() {
        Log.v(tag, "generateComparator()");
        return new Comparator<Task>() {

            @Override
            public int compare(Task lhs, Task rhs) {
                if (lhs.isDone() && rhs.isDone()) {

                    int priority = lhs.isUrgent().compareTo(rhs.isUrgent());
                    if (priority != 0) {
                        return priority;
                    } else if (sortLocation //
                            && mRadius != null //
                            && currentLocation != null//
                            && System.currentTimeMillis() - currentLocation.getTime() < 1000 * 60 * 15) {
                        // TODO get update time
                        Location lhsLocation = lhs.getPlacemark().getLocation();
                        Location rhsLocation = rhs.getPlacemark().getLocation();
                        float lhsDistance = lhsLocation.distanceTo(currentLocation);
                        float rhsDistance = rhsLocation.distanceTo(currentLocation);
                        boolean exit = (lhsDistance > mRadius && rhsDistance > mRadius && lhsDistance == rhsDistance);
                        if (!exit) {
                            float diffDist = lhsDistance - rhsDistance;
                            int roundDiffDist = Math.round(diffDist);
                            return roundDiffDist;
                        }
                    } else {
                        return lhs.getCreated().compareTo(rhs.getCreated());
                    }
                } else {

                    int done = lhs.isDone().compareTo(rhs.isDone());
                    if (done != 0) {
                        return done;
                    }

                    int time = lhs.getCreated().compareTo(rhs.getCreated());
                    if (time != 0) {
                        return time;
                    }
                }
                return 0;
            }
        };
    }

    public ArrayList<Task> getConfiguredTasks() {
        return showDone ? mAllTasks : mDoneLessTasks;
    }

    @Override
    public int getCount() {
        Log.v(tag, "getCount()");
        int size = getConfiguredTasks().size();
        Log.d(tag, "size= " + size);
        return size;
    }

    @Override
    public Task getItem(int position) {
        Log.v(tag, "getItem()");
        Log.v(tag, "position = " + position);
        try {
            return getConfiguredTasks().get(position);
        } catch (IndexOutOfBoundsException tr) {
            Log.e(tag, Log.getStackTraceString(tr));
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        Log.v(tag, "getItemId()");
        Log.v(tag, "position = " + position);
        try {
            Task task = getConfiguredTasks().get(position);
            return task.getId();
        } catch (IndexOutOfBoundsException tr) {
            Log.e(tag, Log.getStackTraceString(tr));
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(tag, "getView()");
        TaskViewHolder viewHolder = null;
        Task task = getItem(position);
        Long taskID = getItemId(position);

        if (convertView == null
                || convertView.getTag(R.id.TAG_TASKVIEW) == null) {
            convertView = mInflater.inflate(
                    R.layout.fragment_tasklist_item, parent, false);
            viewHolder = new TaskViewHolder();
            viewHolder.task_title = (TextView) convertView
                    .findViewById(R.id.task_item_title);
            viewHolder.task_timer = (TextView) convertView
                    .findViewById(R.id.task_item_timer);
            viewHolder.task_image = (ImageView) convertView
                    .findViewById(R.id.task_item_image);

        } else {
            viewHolder = (TaskViewHolder) convertView.getTag(R.id.TAG_TASKVIEW);
        }

        if (task.getImageName() == null) {
            viewHolder.task_image.setImageDrawable(mContext.getResources().getDrawable(
                    R.drawable.hospital));
        } else {
            File imagePath = mContext.getFileStreamPath(task.getImageName());
            viewHolder.task_image.setImageDrawable(Drawable.createFromPath(imagePath.toString()));
        }

        viewHolder.task_title.setText(task.getTitle());

        viewHolder.task_timer.setText(android.text.format.DateUtils
                .getRelativeTimeSpanString(task.getCreated()));
        // TODO
        convertView.setTag(R.id.TAG_TASKVIEW, viewHolder);
        convertView.setTag(R.id.TAG_TASK_ID, taskID);

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        Log.v(tag, "notifyDataSetChanged()");
        mLastRefresh = SystemClock.currentThreadTimeMillis();
        super.notifyDataSetChanged();
    }

    public void setLocation(Location location) {
        if (location.equals(currentLocation)) {
            return;
        }
        currentLocation.reset();
        currentLocation.set(location);
    }

    private void setLocationPolling() {

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);

        String bestProvider = locationManager.getBestProvider(criteria, true);

        Intent poller = new Intent(mContext, LocationPoller.class);
        poller.putExtra(LocationPoller.EXTRA_INTENT, new Intent(mContext,
                LocationMonitor.class));
        poller.putExtra(LocationPoller.EXTRA_PROVIDER, bestProvider);
        pendingPoller = PendingIntent.getBroadcast(mContext, 0, poller, 0);
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
                1000 * 60 * 5, pendingPoller);
    }

    public void setRadius(Integer radius) {
        if (radius.equals(mRadius)) {
            return;
        }
        mRadius = radius;
        // TODO update radius dependante operations
    }

    private void setupSharedPreference() {
        tasklistListener = new OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(
                    SharedPreferences sharedPreferences, String key) {
                Log.v(tag, "onSharedPreferenceChanged()");
                String keyShowDone = mContext.getResources().getString(
                        R.string.sp_tasklist_key_show_done);
                String keyLocationSort = mContext.getResources().getString(
                        R.string.sp_tasklist_key_sort_location);
                boolean notify = false;
                if (key.equals(keyShowDone)) {
                    showDone = sharedPreferences.getBoolean(key, true);
                    notify = true;
                } else if (key.equals(keyLocationSort)) {
                    sortLocation = sharedPreferences.getBoolean(key, true);
                    sort();
                    notify = true;
                }
                if (notify) {
                    notifyDataSetChanged();
                }
            }
        };
        String spTaskList = mContext.getResources().getString(R.string.sp_tasklist_name);
        mSharedPreferences = mContext.getSharedPreferences(spTaskList
                , Context.MODE_PRIVATE);
        String keyShowDone = mContext.getResources().getString(R.string.sp_tasklist_key_show_done);
        showDone = mSharedPreferences.getBoolean(keyShowDone, true);
        String keyLocationSort = mContext.getResources().getString(
                R.string.sp_tasklist_key_sort_location);
        sortLocation = mSharedPreferences.getBoolean(keyLocationSort, true);
        mSharedPreferences
                .registerOnSharedPreferenceChangeListener(tasklistListener);

        // Status shared prefs
        statusListener = new OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                String keyisCharging = mContext.getResources().getString(
                        R.string.sp_status_key_is_charging);
                String keyBatteryOkey = mContext.getResources().getString(
                        R.string.sp_status_key_battery_okey);
                if (key.equals(keyisCharging)) {
                    isCharging = sharedPreferences.getBoolean(key, isCharging);
                } else if (key.equals(keyBatteryOkey)) {
                    batteryOkey = sharedPreferences.getBoolean(key, batteryOkey);
                }
            }
        };

        String spStatus = mContext.getResources().getString(R.string.sp_status_name);
        mSharedPreferences = mContext.getSharedPreferences(spStatus, Context.MODE_PRIVATE);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(statusListener);

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = mContext.registerReceiver(null, ifilter);

        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        // Hardcoded 15% low battery level
        final Integer LOW_LEVEL = 15;
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        batteryOkey = (level / (float) scale) > LOW_LEVEL ? true : false;
    }

    private void sort() {
        Log.v(tag, "sort()");
        Comparator<Task> taskComparator = generateComparator();
        // TODO support done stripping
        Collections.sort(mAllTasks, taskComparator);
        Collections.sort(mDoneLessTasks, taskComparator);
    }

    private void unsetLocationPolling() {
        alarm.cancel(pendingPoller);
    }

    public void updateDataSet(ArrayList<Task> newTasks) {

        if (newTasks.equals(mAllTasks)) {
            return;
        }
        mAllTasks.clear();
        mDoneLessTasks.clear();
        mAllTasks.addAll(newTasks);
        for (Task task : newTasks) {
            if (!task.isDone()) {
                mDoneLessTasks.add(task);
            }
        }
    }
}
