
package com.tunav.tunavmedi.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.datatype.Patient;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PatientsAdapter extends BaseAdapter implements OnSharedPreferenceChangeListener {

    static class TaskViewHolder {
        TextView patient_name;
        TextView patient_timer;
        ImageView patient_photo;
    }

    private static final String tag = "PatientsAdapter";

    private volatile ArrayList<Patient> mAllPatients = new ArrayList<Patient>();

    private volatile ArrayList<Patient> mRemindPatients = new ArrayList<Patient>();

    private Context mContext = null;
    private final LayoutInflater mInflater;
    private Boolean showChecked;
    private Boolean locationSort;
    private Handler handler;
    private String keyShowDone;
    private String keyLocationSort;
    private Location currentLocation = null;
    private Integer mRadius = null;

    private final Integer MINUT = 1000 * 60;

    private Runnable refreshList = new Runnable() {
        private static final String tag = "refreshList";

        @Override
        public void run() {
            Log.v(tag, "Refreshing Patient List");
            handler.postDelayed(refreshList, 3 * MINUT);
            notifyDataSetChanged();
        }
    };

    private Object keyLocationUpdate;

    public PatientsAdapter(Context context) {
        Log.v(tag, "TasksAdapter()");
        mContext = context;

        keyShowDone = mContext.getResources().getString(
                R.string.spkey_show_checked);
        keyLocationSort = mContext.getResources().getString(
                R.string.spkey_sort_location);
        keyLocationUpdate = mContext.getResources().getString(R.string.spkey_location_update);
        mInflater = LayoutInflater.from(mContext);

        String spPatientsList = mContext.getResources().getString(R.string.sp_patientlist);
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(spPatientsList
                , Context.MODE_PRIVATE);
        showChecked = sharedPrefs.getBoolean(keyShowDone, true);
        locationSort = sharedPrefs.getBoolean(keyLocationSort, true);
        sharedPrefs
                .registerOnSharedPreferenceChangeListener(this);

        handler = new Handler();
        handler.postDelayed(refreshList, 3 * MINUT);
    }

    private Comparator<Patient> generateComparator() {
        Log.v(tag, "generateComparator()");
        return new Comparator<Patient>() {

            @Override
            public int compare(Patient lhs, Patient rhs) {
                if (lhs.alarmeOn() && rhs.alarmeOn()) {

                    int priority = lhs.isUrgent().compareTo(rhs.isUrgent());
                    if (priority != 0) {
                        return priority;
                    } else if (locationSort //
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
                        return lhs.getInTime().compareTo(rhs.getInTime());
                    }
                } else {

                    int remind = lhs.alarmeOn().compareTo(rhs.alarmeOn());
                    if (remind != 0) {
                        return remind;
                    }

                    int time = lhs.getInTime().compareTo(rhs.getInTime());
                    if (time != 0) {
                        return time;
                    }
                }
                return 0;
            }
        };
    }

    public ArrayList<Patient> getConfiguredTasks() {
        return showChecked ? mAllPatients : mRemindPatients;
    }

    @Override
    public int getCount() {
        Log.v(tag, "getCount()");
        int size = getConfiguredTasks().size();
        Log.d(tag, "size= " + size);
        return size;
    }

    @Override
    public Patient getItem(int position) {
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
            Patient task = getConfiguredTasks().get(position);
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
        Patient task = getItem(position);
        Long taskID = getItemId(position);

        if (convertView == null
                || convertView.getTag(R.id.TAG_TASKVIEW) == null) {
            convertView = mInflater.inflate(
                    R.layout.fragment_tasklist_item, parent, false);
            viewHolder = new TaskViewHolder();
            assert convertView != null;
            viewHolder.patient_name = (TextView) convertView
                    .findViewById(R.id.task_item_title);
            viewHolder.patient_timer = (TextView) convertView
                    .findViewById(R.id.task_item_timer);
            viewHolder.patient_photo = (ImageView) convertView
                    .findViewById(R.id.task_item_image);

        } else {
            viewHolder = (TaskViewHolder) convertView.getTag(R.id.TAG_TASKVIEW);
        }

        if (task.getPhoto() == null) {
            viewHolder.patient_photo.setImageDrawable(mContext.getResources().getDrawable(
                    R.drawable.hospital));
        } else {
            File imagePath = mContext.getFileStreamPath(task.getPhoto());
            viewHolder.patient_photo
                    .setImageDrawable(Drawable.createFromPath(imagePath.toString()));
        }

        viewHolder.patient_name.setText(task.getName());

        viewHolder.patient_timer.setText(android.text.format.DateUtils
                .getRelativeTimeSpanString(task.getInTime()));
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.v(tag, "onSharedPreferenceChanged()");

        if (key.equals(keyShowDone)) {
            showChecked = sharedPreferences.getBoolean(key, true);
        } else if (key.equals(keyLocationSort)) {
            locationSort = sharedPreferences.getBoolean(key, true);
        } else {
            return;
        }
        sort();
        notifyDataSetChanged();
    }

    public void setLocation(Location location) {
        Log.v(tag, "setLocation()");
        currentLocation = new Location(location);
    }

    public void setRadius(Integer radius) {
        Log.v(tag, "setRadius()");
        mRadius = radius;
    }

    private void sort() {
        Log.v(tag, "sort()");
        Comparator<Patient> taskComparator = generateComparator();
        Collections.sort(mAllPatients, taskComparator);
        Collections.sort(mRemindPatients, taskComparator);
    }

    public void updateDataSet(ArrayList<Patient> newPatients) {
        Log.v(tag, "updateDataSet()");
        if (!newPatients.equals(mAllPatients)) {
            mAllPatients.clear();
            mRemindPatients.clear();
            mAllPatients.addAll(newPatients);
            for (Patient patient : newPatients) {
                if (!patient.alarmeOn()) {
                    mRemindPatients.add(patient);
                }
            }
            sort();
            notifyDataSetChanged();
        }
    }
}
