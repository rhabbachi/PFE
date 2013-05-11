
package com.tunav.tunavmedi.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.abstraction.TasksHandler;
import com.tunav.tunavmedi.app.TunavMedi;
import com.tunav.tunavmedi.datatype.Task;
import com.tunav.tunavmedi.demo.sqlite.helper.TasksHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TasksAdapter extends BaseAdapter {
    static class TaskViewHolder {
        TextView task_title;
        TextView task_timer;
        ImageView task_image;
    }

    private static final String tag = "TasksAdapter";

    private Comparator<Task> taskComparator = new Comparator<Task>() {

        @Override
        public int compare(Task lhs, Task rhs) {
            // done task are sorted down
            int done = lhs.getStatus().compareTo(rhs.getStatus());
            if (done != 0) {
                return done;
            }
            // priority sort
            int priority = lhs.getPriority().compareTo(rhs.getPriority());
            if (priority != 0) {
                return priority;
            }

            if (sortLocation) {
                int location = lhs.getPlacemark().compareTo(rhs.getPlacemark());
                if (location != 0) {
                    return location;
                }
            }
            return 0;
        }

    };

    private Context mContext = null;
    private final LayoutInflater mInflater;
    private TasksHandler mHelper = null;
    private ArrayList<Task> mTasksList = null;
    private boolean showDone = false;
    private boolean sortLocation = true;
    private SharedPreferences mSharedPreferences = null;
    private Location currentLocation = null;
    private final OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener = new OnSharedPreferenceChangeListener() {

        @Override
        public void onSharedPreferenceChanged(
                SharedPreferences sharedPreferences, String key) {
            Log.v(tag, "onSharedPreferenceChanged()");

            String spDone = mContext.getResources().getString(
                    R.string.tasklist_sp_show_done);
            String spLocation = mContext.getResources().getString(
                    R.string.tasklist_sp_sort_location);
            if (key == spDone) {
                showDone = sharedPreferences.getBoolean(spDone, true);
            } else if (key == spLocation) {
                sortLocation = sharedPreferences.getBoolean(spLocation, true);
                sort();
            } else {
                return;
            }
            notifyDataSetChanged();
        }
    };

    public TasksAdapter(Context context) {
        Log.v(tag, "TasksAdapter()");
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mHelper = new TasksHelper(mContext);
        mTasksList = mHelper.getTasks();
        mSharedPreferences = mContext.getSharedPreferences(
                TunavMedi.SHAREDPREFS_NAME, Context.MODE_PRIVATE);
        mSharedPreferences
                .registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
    }

    @Override
    public int getCount() {
        Log.v(tag, "getCount()");
        int size = mTasksList.size();
        Log.d(tag, "size= " + size);
        return size;
    }

    @Override
    public Task getItem(int position) {
        Log.v(tag, "getItem()");
        Log.v(tag, "position = " + position);
        try {
            return mTasksList.get(position);
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
            Task task = mTasksList.get(position);
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
                    R.layout.tasklist_item, parent, false);
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
                .getRelativeTimeSpanString(task.getCreationDate().getTime()));
        // TODO

        convertView.setTag(R.id.TAG_TASKVIEW, viewHolder);
        convertView.setTag(R.id.TAG_TASK_ID, taskID);

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        Log.v(tag, "notifyDataSetChanged()");
        super.notifyDataSetChanged();
    }

    private void sort() {
        Log.v(tag, "sort()");
        Collections.sort(mTasksList, taskComparator);
    }

    public void updateDataSet() {
        Log.v(tag, "updateDataSet()");
        mTasksList = mHelper.getTasks();
        notifyDataSetChanged();
    }

    private void updateLocation() {
        LocationManager locationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);

        String bestProvider = locationManager.getBestProvider(criteria, false);
    }
}
