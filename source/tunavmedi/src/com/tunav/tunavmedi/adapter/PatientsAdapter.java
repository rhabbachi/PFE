
package com.tunav.tunavmedi.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.TunavMedi;
import com.tunav.tunavmedi.dal.datatype.Patient;
import com.tunav.tunavmedi.service.PatientsService.PatientsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PatientsAdapter extends BaseAdapter implements OnSharedPreferenceChangeListener,
        PatientsListener {

    static class TaskViewHolder {
        TextView patient_name;
        TextView patient_distance;
        ImageView patient_photo;
    }

    private static final String tag = "PatientsAdapter";

    private volatile ArrayList<Patient> mAllPatients = new ArrayList<Patient>();
    private volatile ArrayList<Patient> mUrgentPatients = new ArrayList<Patient>();
    private Context mContext = null;
    private final LayoutInflater mInflater;
    private Boolean showUrgent;
    private Boolean locationSort;
    private Location currentLocation = null;

    public PatientsAdapter(Context context) {
        Log.v(tag, "TasksAdapter()");
        mContext = context;

        mInflater = LayoutInflater.from(mContext);

        String spPatientsList = mContext.getResources().getString(R.string.sp_patientlist);
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(spPatientsList
                , Context.MODE_PRIVATE);
        showUrgent = sharedPrefs.getBoolean(mContext.getResources().getString(
                R.string.spkey_show_all), true);
        locationSort = sharedPrefs.getBoolean(mContext.getResources().getString(
                R.string.spkey_sort_by_location), true);
        sharedPrefs
                .registerOnSharedPreferenceChangeListener(this);

    };

    private Comparator<Patient> generateComparator() {
        Log.v(tag, "generateComparator()");
        return new Comparator<Patient>() {

            int EQL = 0;
            int SUP = 1;
            int INF = -1;

            @Override
            public int compare(Patient rhs, Patient lhs) {
                int result = 0;
                try {
                    result = lhs.isUrgent().compareTo(rhs.isUrgent());
                    if (result == 0 && locationSort && currentLocation != null) {
                        if (lhs.getPlacemark() == null
                                && rhs.getPlacemark() == null) {
                            result = EQL;
                        } else if (lhs.getPlacemark() == null
                                && rhs.getPlacemark() != null) {
                            result = INF;
                        } else if (lhs.getPlacemark() != null
                                && rhs.getPlacemark() == null) {
                            result = SUP;
                        } else if (lhs.getPlacemark() != null
                                && rhs.getPlacemark() != null) {
                            Location lhsLocation = lhs.getPlacemark().getLocation();
                            Location rhsLocation = rhs.getPlacemark().getLocation();
                            float lhsDistance = lhsLocation.distanceTo(currentLocation);
                            float rhsDistance = rhsLocation.distanceTo(currentLocation);
                            float diffDist = rhsDistance - lhsDistance;
                            if (diffDist > 0) {
                                result = SUP;
                            } else if (diffDist < 0) {
                                result = INF;
                            } else {
                                result = EQL;
                            }
                        }
                    }
                    if (result == 0) {
                        result = lhs.getInterned().compareTo(rhs.getInterned()) * (-1);
                    }
                } catch (NullPointerException tr) {
                    Log.e(tag, "NullPointerException");
                    Log.d(tag, "NullPointerException", tr);
                }
                return result;
            }
        };
    }

    public ArrayList<Patient> getConfiguredTasks() {
        return showUrgent ? mUrgentPatients : mAllPatients;
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
        Patient patient = getItem(position);
        Long patientID = getItemId(position);

        if (convertView == null
                || convertView.getTag(R.id.TAG_TASKVIEW) == null) {
            convertView = mInflater.inflate(
                    R.layout.fragment_patientlist_item, parent, false);
            viewHolder = new TaskViewHolder();
            assert convertView != null;
            viewHolder.patient_name = (TextView) convertView
                    .findViewById(R.id.task_item_title);
            viewHolder.patient_distance = (TextView) convertView
                    .findViewById(R.id.task_item_timer);
            viewHolder.patient_photo = (ImageView) convertView
                    .findViewById(R.id.task_item_image);

        } else {
            viewHolder = (TaskViewHolder) convertView.getTag(R.id.TAG_TASKVIEW);
        }

        if (patient.getPhoto() == null) {
            viewHolder.patient_photo.setImageDrawable(mContext.getResources().getDrawable(
                    R.drawable.hospital));
        } else {
            File imagePath = mContext.getFileStreamPath(patient.getPhoto());
            viewHolder.patient_photo
                    .setImageDrawable(Drawable.createFromPath(imagePath.toString()));
        }

        viewHolder.patient_name.setText(patient.getName());

        Location patientLocation = patient.getPlacemark().getLocation();

        if (currentLocation != null && patientLocation != null) {
            if (patientLocation.distanceTo(currentLocation) < TunavMedi.OUT_OF_REACH) {
                float dist = patientLocation.distanceTo(currentLocation);
                viewHolder.patient_distance.setText((int) Math.ceil(dist) + "m");
            } else {
                viewHolder.patient_distance.setText("too far");
            }
        } else {
            viewHolder.patient_distance.setText("Unknown Location");
        }
        if (patient.isUrgent()) {
            viewHolder.patient_name.setTextColor
                    (mContext.getResources().getColor(
                            R.color.patient_background_urgent));
        } else {
            viewHolder.patient_name.setTextColor(mContext.getResources().getColor(
                    R.color.patient_background));
        }

        convertView.setTag(R.id.TAG_TASKVIEW, viewHolder);
        convertView.setTag(R.id.TAG_TASK_ID, patientID);

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

        if (key.equals(mContext.getResources().getString(
                R.string.spkey_show_all))) {
            showUrgent = sharedPreferences.getBoolean(key, true);
        } else if (key.equals(mContext.getResources().getString(
                R.string.spkey_sort_by_location))) {
            locationSort = sharedPreferences.getBoolean(key, true);
        } else {
            return;
        }
        sort();
    }

    private void sort() {
        Log.v(tag, "sort()");
        Comparator<Patient> taskComparator = generateComparator();
        Collections.sort(mAllPatients, taskComparator);
        Collections.sort(mUrgentPatients, taskComparator);
        notifyDataSetChanged();
    }

    @Override
    public void updateDataSet(ArrayList<Patient> newPatients, Location newLocation) {
        Log.v(tag, "updateDataSet()");
        mAllPatients.clear();
        mUrgentPatients.clear();
        mAllPatients.addAll(newPatients);
        for (Patient patient : newPatients) {
            if (patient.isUrgent()) {
                mUrgentPatients.add(patient);
            }
        }

        if (newLocation == null) {
            currentLocation = null;
        } else {
            currentLocation = new Location(newLocation);
        }
        sort();
    }
}
