
package com.tunav.tunavmedi.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tunav.tunavmedi.R;

public class TaskDialog extends DialogFragment {

    private static final String tag = "TaskPopup";

    public static final String ARG_ICON_NAME = "icon_name";
    public static final String ARG_DESCRIPTION = "description";
    public static final String ARG_DESCRIPTION_DEFAULT = "No Description!";
    public static final String ARG_TITLE = "title";
    public static final String ARG_TITLE_DEFAULT = "No Title!";
    public static final String DEFAULT_ICON_NAME = "hospital";
    public static final String ARG_TIME = "time";

    static TaskDialog newInstance(String title, String iconName,
            String description, Long time) {
        TaskDialog taskPopup = new TaskDialog();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_ICON_NAME, iconName);
        args.putString(ARG_DESCRIPTION, description);
        args.putLong(ARG_TIME, time);
        taskPopup.setArguments(args);

        return taskPopup;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View taskView = inflater.inflate(R.layout.task_dialog, container, false);

        TextView taskTitle = (TextView) taskView.findViewById(R.id.task_dialog_title);
        ImageView taskImage = (ImageView) taskView.findViewById(R.id.task_dialog_image);
        TextView taskTimer = (TextView) taskView.findViewById(R.id.task_dialog_timer);
        TextView taskDescription = (TextView)
                taskView.findViewById(R.id.task_dialog_description);

        taskTitle.setText(getArguments().getString(ARG_TITLE, ARG_TITLE_DEFAULT));
        taskImage.setImageDrawable(getResources().getDrawable(R.drawable.hospital));
        taskTimer.setText(android.text.format.DateUtils
                .getRelativeTimeSpanString(getArguments().getLong(ARG_TIME)));
        taskDescription.setText(getArguments().getString(ARG_DESCRIPTION,
                ARG_DESCRIPTION_DEFAULT));

        return taskView;
    }

}
