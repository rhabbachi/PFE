
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
import com.tunav.tunavmedi.datatype.Task;

public class TaskDisplay extends DialogFragment {

    public static final String tag = "TaskDisplay";

    public static final String ARG_ICON_NAME = "icon_name";
    public static final String ARG_DESCRIPTION = "description";
    public static final String ARG_DESCRIPTION_DEFAULT = "No Description!";
    public static final String ARG_TITLE = "title";
    public static final String ARG_TITLE_DEFAULT = "No Title!";
    public static final String DEFAULT_ICON_NAME = "hospital";
    public static final String ARG_TIME = "time";

    public static TaskDisplay newInstance(Task task) {
        TaskDisplay taskDisplay = new TaskDisplay();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, task.getTitle());
        args.putString(ARG_ICON_NAME, task.getImageName());
        args.putString(ARG_DESCRIPTION, task.getDescription());
        args.putLong(ARG_TIME, task.getCreated());
        taskDisplay.setArguments(args);

        return taskDisplay;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View taskView = inflater.inflate(R.layout.fragment_task_display, container, false);

        TextView taskTitle = (TextView) taskView.findViewById(R.id.task_dialog_title);
        ImageView taskImage = (ImageView) taskView.findViewById(R.id.task_dialog_image);
        TextView taskTimer = (TextView) taskView.findViewById(R.id.task_dialog_timer);
        WebView taskDescription = (WebView)
                taskView.findViewById(R.id.task_dialog_description);

        taskTitle.setText(getArguments().getString(ARG_TITLE, ARG_TITLE_DEFAULT));
        taskImage.setImageDrawable(getResources().getDrawable(R.drawable.hospital));
        taskTimer.setText(android.text.format.DateUtils
                .getRelativeTimeSpanString(getArguments().getLong(ARG_TIME)));
        String description = getArguments().getString(ARG_DESCRIPTION,
                ARG_DESCRIPTION_DEFAULT);
        taskDescription.loadDataWithBaseURL("file:///android_asset/", description, "text/html",
                "utf-8", null);

        return taskView;
    }
}
