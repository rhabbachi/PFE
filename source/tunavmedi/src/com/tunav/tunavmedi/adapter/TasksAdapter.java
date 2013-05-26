
package com.tunav.tunavmedi.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.datatype.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TasksAdapter extends BaseAdapter {

    static class TaskViewHolder {
        TextView task_title;
        TextView task_timer;
        ImageView task_image;
    }

    private static final String tag = "TasksAdapter";
    private volatile ArrayList<Task> mAllTasks = new ArrayList<Task>();
    private volatile ArrayList<Task> mDoneLessTasks = new ArrayList<Task>();
    private Context mContext = null;
    private final LayoutInflater mInflater;
    private Boolean showDone = null;
    private Boolean sortLocation = null;
    private SharedPreferences mSharedPreferences = null;
    private Location currentLocation = null;
    private Integer mRadius = null;

    private OnSharedPreferenceChangeListener tasklistListener;

    private ScheduledExecutorService mScheduler = null;
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

        setupSharedPreference();
        mScheduler = Executors.newSingleThreadScheduledExecutor();
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
                            return Math.round(diffDist);
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
        TaskViewHolder viewHolder;
        Task task = getItem(position);
        Long taskID = getItemId(position);

        if (convertView == null
                || convertView.getTag(R.id.TAG_TASKVIEW) == null) {
            convertView = mInflater.inflate(
                    R.layout.fragment_tasklist_item, parent, false);
            viewHolder = new TaskViewHolder();
            assert convertView != null;
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
    }

    private void sort() {
        Log.v(tag, "sort()");
        Comparator<Task> taskComparator = generateComparator();
        // TODO support done stripping
        Collections.sort(mAllTasks, taskComparator);
        Collections.sort(mDoneLessTasks, taskComparator);
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
        notifyDataSetChanged();
    }
}
