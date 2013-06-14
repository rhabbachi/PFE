
package com.tunav.tunavmedi.fragment.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.TunavMedi;

import java.util.Random;

public class RandomFakeLocation extends DialogFragment implements OnCheckedChangeListener {
    public static final String tag = "RandomFakeLocation";

    public static String BDL_FAKE_LATITUDE = "fake_latitude";
    public static String BDL_FAKE_LONGITUDE = "fake_longitude";
    public static String BDL_FAKE_ALTITUDE = "fake_altitude";

    public static RandomFakeLocation newInstance() {
        return new RandomFakeLocation();
    }

    public static RandomFakeLocation newInstance(double fakeLatitude, double fakeLongitude,
            double fakeAltitude) {
        RandomFakeLocation dialog = new RandomFakeLocation();

        Bundle args = new Bundle();
        args.putDouble(BDL_FAKE_LATITUDE, fakeLatitude);
        args.putDouble(BDL_FAKE_LONGITUDE, fakeLongitude);
        args.putDouble(BDL_FAKE_ALTITUDE, fakeAltitude);

        dialog.setArguments(args);
        return dialog;
    }

    private CheckBox fakeLocationCheck;

    double randomLongitude;

    double randomLatitude;

    TextView textviewLocation;

    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            double rangeMinLatitude = TunavMedi.getDebugLocation()[TunavMedi.LOCATION_MIN][TunavMedi.LOCATION_LATITUDE];
            double rangeMaxLatitude = TunavMedi.getDebugLocation()[TunavMedi.LOCATION_MAX][TunavMedi.LOCATION_LATITUDE];

            double rangeMinLongitude = TunavMedi.getDebugLocation()[TunavMedi.LOCATION_MIN][TunavMedi.LOCATION_LONGITUDE];
            double rangeMaxLongitude = TunavMedi.getDebugLocation()[TunavMedi.LOCATION_MAX][TunavMedi.LOCATION_LONGITUDE];

            Random r = new Random();
            double nextDouble = r.nextDouble();

            randomLatitude = rangeMinLatitude + (rangeMaxLatitude - rangeMinLatitude)
                    * nextDouble;
            randomLongitude = rangeMinLongitude + (rangeMaxLongitude - rangeMinLongitude)
                    * nextDouble;

            String text = "Latitude: " + Double.toString(randomLatitude) + " ; " + "Longitude: "
                    + Double.toString(randomLongitude);
            textviewLocation.setText(text);
        }
    };

    Button generate;

    private void disableFakeLocation() {

        fakeLocationCheck.setChecked(false);
        generate.setEnabled(false);
        textviewLocation.setEnabled(false);
        setSharedPrefsFakeLocationStatus(false);
    }

    private void enableFakeLocation() {

        fakeLocationCheck.setChecked(true);
        generate.setEnabled(true);
        textviewLocation.setEnabled(true);
        setSharedPrefsFakeLocationStatus(true);
    }

    private boolean getSharedPrefsFakeLocationStatus(boolean defValue) {
        String name = getActivity().getResources().getString(R.string.sp_dev);
        int mode = Context.MODE_PRIVATE;
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences(name, mode);

        String key = getActivity().getResources().getString(R.string.spkey_fakelocation);
        return sharedPrefs.getBoolean(key, defValue);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkbox_enbale_fakelocation:
                setSharedPrefsFakeLocationStatus(isChecked);
                if (isChecked) {
                    enableFakeLocation();
                } else {
                    disableFakeLocation();
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.v(tag, "onCreateView()");

        getDialog().setTitle(R.string.menu_item_fake_location_title);

        getDialog().setCanceledOnTouchOutside(true);
        View hospitalMap = inflater.inflate(R.layout.fragment_fakelocation_random, container);

        Location fakeLocation = TunavMedi.devFakeLocation;
        // double altitude;
        double randomLatitude;
        double randomLongitude;
        if (fakeLocation == null) {
            randomLatitude = TunavMedi.getDebugLocation()[TunavMedi.LOCATION_NOMINAL][TunavMedi.LOCATION_LATITUDE];
            randomLongitude = TunavMedi.getDebugLocation()[TunavMedi.LOCATION_NOMINAL][TunavMedi.LOCATION_LONGITUDE];
        } else {
            randomLatitude = fakeLocation.getLatitude();
            randomLongitude = fakeLocation.getLongitude();
        }

        textviewLocation = (TextView) hospitalMap.findViewById(R.id.textview_random);
        String text = "Latitude: " + Double.toString(randomLatitude) + " ; " + "Longitude: "
                + Double.toString(randomLongitude);
        textviewLocation.setText(text);
        textviewLocation.setFocusable(false);

        generate = (Button) hospitalMap.findViewById(R.id.button_generate);
        generate.setOnClickListener(listener);

        fakeLocationCheck = (CheckBox) hospitalMap
                .findViewById(R.id.checkbox_enbale_fakelocation);

        if (getSharedPrefsFakeLocationStatus(false)) {
            enableFakeLocation();
        } else {
            disableFakeLocation();
        }

        fakeLocationCheck.setOnCheckedChangeListener(this);

        return hospitalMap;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (getSharedPrefsFakeLocationStatus(false)) {
            Intent intent = new Intent();

            intent.putExtra(
                    BDL_FAKE_LATITUDE,
                    randomLatitude);

            intent.putExtra(
                    BDL_FAKE_LONGITUDE,
                    randomLongitude);

            getTargetFragment().onActivityResult(getTargetRequestCode(),
                    Activity.RESULT_OK, intent);
        } else {
            getTargetFragment().onActivityResult(getTargetRequestCode(),
                    Activity.RESULT_CANCELED, null);
        }
    }

    private void setSharedPrefsFakeLocationStatus(boolean value) {
        String name = getActivity().getResources().getString(R.string.sp_dev);
        int mode = Context.MODE_PRIVATE;
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences(name, mode);

        Editor editor = sharedPrefs.edit();

        String key = getActivity().getResources().getString(R.string.spkey_fakelocation);

        editor.putBoolean(key, value);

        editor.commit();
    }
}
