
package com.tunav.tunavmedi.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.Task;
import com.tunav.tunavmedi.TunavMedi;
import com.tunav.tunavmedi.abstraction.TasksHandler;
import com.tunav.tunavmedi.demo.sqlite.helper.TasksHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TasksAdapter extends BaseAdapter {
    private static final String tag = "TasksAdapter";

    private Context mContext = null;
    private final LayoutInflater mInflater;
    private TasksHandler mHelper = null;
    private ArrayList<Task> mTasksList = null;
    private boolean showDone = false;
    private boolean sortPlacemark = true;
    private SharedPreferences mSharedPreferences = null;
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
                sortPlacemark = sharedPreferences.getBoolean(spLocation, true);
            } else {
                return;
            }
            sort();
            notifyDataSetChanged();
        }
    };

    // Elegant sort technique
    private enum TasksSort implements Comparator<Task> {
        SORT_DONE {
            @Override
            public int compare(Task lhs, Task rhs) {
                return lhs.getStatus().compareTo(rhs.getStatus());
            }
        },

        SORT_PRIORITY {
            @Override
            public int compare(Task lhs, Task rhs) {
                return lhs.getPriority().compareTo(rhs.getPriority());
            }
        },

        SORT_LOCATION {
            @Override
            public int compare(Task lhs, Task rhs) {
                return lhs.getPlacemark().compareTo(rhs.getPlacemark());
            }
        };
    }

    public static Comparator<Task> getComparator(
            final TasksSort... multipleOptions) {
        return new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                for (TasksSort option : multipleOptions) {
                    int result = option.compare(o1, o2);
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            }
        };
    }

    // Nice and beautiful!

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

    static class TaskViewHolder {
        TextView task_title;
        TextView task_timer;
        ImageView task_image;
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
                    .findViewById(R.id.task_title);
            viewHolder.task_timer = (TextView) convertView
                    .findViewById(R.id.task_timer);
            viewHolder.task_image = (ImageView) convertView
                    .findViewById(R.id.task_image);

        } else {
            viewHolder = (TaskViewHolder) convertView.getTag(R.id.TAG_TASKVIEW);
        }

        if (task.getDrawable() == null) {
            viewHolder.task_image.setImageDrawable(mContext.getResources().getDrawable(
                    R.drawable.hospital));
        } else {
            viewHolder.task_image.setImageDrawable(task.getDrawable());
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

    public void updateDataSet() {
        Log.v(tag, "updateDataSet()");
        mTasksList = mHelper.getTasks();
        notifyDataSetChanged();
    }

    private void sort() {
        Log.v(tag, "sort()");
        Collections.sort(mTasksList, getComparator(TasksSort.SORT_DONE));
    }
}
