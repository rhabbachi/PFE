
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
import com.tunav.tunavmedi.service.PatientsService.PatientsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PatientsAdapter extends BaseAdapter implements OnSharedPreferenceChangeListener,
        PatientsListener {

    static class TaskViewHolder {
        TextView patient_name;
        TextView patient_timer;
        ImageView patient_photo;
    }

    private static final String tag = "PatientsAdapter";

    private volatile ArrayList<Patient> mAllPatients = new ArrayList<Patient>();

    private volatile ArrayList<Patient> mUrgentPatients = new ArrayList<Patient>();

    private Context mContext = null;
    private final LayoutInflater mInflater;
    private Boolean showUrgent;
    private Boolean locationSort;
    private Handler handler;
    private String keyShowDone;
    private String keyLocationSort;
    private Location currentLocation = null;

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

    public PatientsAdapter(Context context) {
        Log.v(tag, "TasksAdapter()");
        mContext = context;

        keyShowDone = mContext.getResources().getString(
                R.string.spkey_show_checked);
        keyLocationSort = mContext.getResources().getString(
                R.string.spkey_sort_location);
        mInflater = LayoutInflater.from(mContext);

        String spPatientsList = mContext.getResources().getString(R.string.sp_patientlist);
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(spPatientsList
                , Context.MODE_PRIVATE);
        showUrgent = sharedPrefs.getBoolean(keyShowDone, true);
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

                int result = lhs.isUrgent().compareTo(rhs.isUrgent());

                if (result == 0 && locationSort && currentLocation != null) {
                    Location lhsLocation = lhs.getPlacemark().getLocation();
                    Location rhsLocation = rhs.getPlacemark().getLocation();
                    float lhsDistance = lhsLocation.distanceTo(currentLocation);
                    float rhsDistance = rhsLocation.distanceTo(currentLocation);
                    float diffDist = lhsDistance - rhsDistance;
                    result = Math.round(diffDist);
                }

                if (result == 0) {
                    result = lhs.getInTime().compareTo(rhs.getInTime());
                }
                return result;
            }
        };
    }

    public ArrayList<Patient> getConfiguredTasks() {
        return showUrgent ? mAllPatients : mUrgentPatients;
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
                    R.layout.fragment_patientlist_item, parent, false);
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
            showUrgent = sharedPreferences.getBoolean(key, true);
        } else if (key.equals(keyLocationSort)) {
            locationSort = sharedPreferences.getBoolean(key, true);
        } else {
            return;
        }
        sort();
        notifyDataSetChanged();
    }

    @Override
    public void setLocation(Location location) {
        Log.v(tag, "setLocation()");
        currentLocation = new Location(location);
    }

    private void sort() {
        Log.v(tag, "sort()");
        Comparator<Patient> taskComparator = generateComparator();
        Collections.sort(mAllPatients, taskComparator);
        Collections.sort(mUrgentPatients, taskComparator);
    }

    @Override
    public void updateDataSet(ArrayList<Patient> newPatients) {
        Log.v(tag, "updateDataSet()");
        mAllPatients.clear();
        mUrgentPatients.clear();
        mAllPatients.addAll(newPatients);
        for (Patient patient : newPatients) {
            if (patient.isUrgent()) {
                mUrgentPatients.add(patient);
            }
        }
        sort();
        notifyDataSetChanged();
    }
}
