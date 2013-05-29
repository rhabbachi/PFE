
package com.tunav.tunavmedi.fragment.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tunav.tunavmedi.R;

public class PatientDisplay extends DialogFragment {

    public static final String tag = "TaskDisplay";

    public static final String ARG_PHOTO = "photo";
    public static final String ARG_DESCRIPTION = "description";
    public static final String ARG_DESCRIPTION_DEFAULT = "No Description!";
    public static final String ARG_NAME = "name";
    public static final String ARG_NAME_DEFAULT = "N/A";
    public static final String DEFAULT_PHOTO = "hospital";
    public static final String ARG_INTERNED = "interned";

    public static PatientDisplay newInstance(String name, String photo, String record,
            Long interned) {
        PatientDisplay patientDisplay = new PatientDisplay();

        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_PHOTO, photo);
        args.putString(ARG_DESCRIPTION, record);
        args.putLong(ARG_INTERNED, interned);
        patientDisplay.setArguments(args);

        return patientDisplay;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View patientView = inflater.inflate(R.layout.fragment_task_display, container, false);

        TextView patientTitle = (TextView) patientView.findViewById(R.id.task_dialog_title);
        ImageView patientImage = (ImageView) patientView.findViewById(R.id.task_dialog_image);
        TextView patientInterned = (TextView) patientView.findViewById(R.id.task_dialog_timer);
        WebView patientRecord = (WebView)
                patientView.findViewById(R.id.task_dialog_description);

        patientTitle.setText(getArguments().getString(ARG_NAME, ARG_NAME_DEFAULT));
        patientImage.setImageDrawable(getResources().getDrawable(R.drawable.hospital));
        patientInterned.setText(android.text.format.DateUtils
                .getRelativeTimeSpanString(getArguments().getLong(ARG_INTERNED)));
        String record = getArguments().getString(ARG_DESCRIPTION,
                ARG_DESCRIPTION_DEFAULT);
        patientRecord.loadDataWithBaseURL("file:///android_asset/", record, "text/html",
                "utf-8", null);

        return patientView;
    }
}
