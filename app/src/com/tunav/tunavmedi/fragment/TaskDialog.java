
package com.tunav.tunavmedi.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TaskDialog extends DialogFragment {

    private static final String tag = "TaskPopup";

    public static final String ARG_ICON_NAME = "icon_name";
    public static final String ARG_DESCRIPTION = "description";
    public static final String ARG_TITLE = "title";
    public static final String DEFAULT_ICON_NAME = "hospital";

    static TaskDialog newInstance(String title, String iconName,
            String description) {
        TaskDialog taskPopup = new TaskDialog();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_ICON_NAME, iconName);
        args.putString(ARG_DESCRIPTION, description);

        taskPopup.setArguments(args);

        return taskPopup;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(tag, "onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.v(tag, "onCreate()");

        // View view = inflater.inflate(R.layout.task, container, false);
        // // setup header
        // // TextView header = (TextView) view.findViewById(R.id.task_title);
        // header.setText(getArguments().getString(ARG_TITLE, "No Title"));
        // // TODO default string
        // File iconFile = getActivity().getFileStreamPath(
        // getArguments().getString(ARG_ICON_NAME, DEFAULT_ICON_NAME));
        // header.setCompoundDrawablesWithIntrinsicBounds(
        // Drawable.createFromPath(iconFile.toString()), null, null, null);

        // setup body
        // TextView body = (TextView) view.findViewById(R.id.task_description);
        // body.setText(getArguments().getString(ARG_DESCRIPTION,
        // "No task details!"));
        // TODO linkifier

        // return view;
        return null;
    }
}
