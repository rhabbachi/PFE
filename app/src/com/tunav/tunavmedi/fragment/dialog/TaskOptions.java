
package com.tunav.tunavmedi.fragment.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;

import com.tunav.tunavmedi.R;

public class TaskOptions extends DialogFragment {
    public static final String tag = "TaskOptions";
    public static final String ARG_POSITION = "position";
    public static final String ARG_ICON_NAME = "icon_name";
    public static final String ARG_TITLE = "title";
    public static final String ARG_TITLE_DEFAULT = "No Title!";
    public static final String DEFAULT_ICON_NAME = "hospital";
    public static final String ARG_TIME = "time";
    public static final String ARG_OPTIONS = "options";

    public static TaskOptions newInstance(String title, String iconName, boolean isUrgent,
            boolean isDone, boolean notify) {
        TaskOptions taskOptions = new TaskOptions();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_ICON_NAME, iconName);
        boolean[] options = {
                isUrgent,
                isDone,
                notify
        };
        args.putBooleanArray(ARG_OPTIONS, options);
        taskOptions.setArguments(args);

        return taskOptions;
    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:
                    Intent intent = new Intent();
                    intent.putExtra(ARG_POSITION, getArguments().getInt(ARG_POSITION));
                    intent.putExtra("isUrgent", choiceValues[0]);
                    intent.putExtra("isDone", choiceValues[1]);
                    intent.putExtra("notify", choiceValues[2]);
                    getTargetFragment().onActivityResult(getTargetRequestCode(),
                            Activity.RESULT_OK, intent);
                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    getTargetFragment().onActivityResult(getTargetRequestCode(),
                            Activity.RESULT_CANCELED, null);
                default:
                    break;
            }
        }
    };
    private OnMultiChoiceClickListener mOnMultiChoiceClickListener = new OnMultiChoiceClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            choiceValues[which] = isChecked;
        }
    };
    boolean[] choiceValues = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        choiceValues = getArguments().getBooleanArray(ARG_OPTIONS);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getArguments().getString(ARG_TITLE, ARG_TITLE_DEFAULT))
                .setMultiChoiceItems(R.array.task_options
                        , choiceValues
                        , mOnMultiChoiceClickListener)
                .setPositiveButton(R.string.task_options_ok, mOnClickListener)
                .setNegativeButton(R.string.task_options_cancel, mOnClickListener);

        return builder.create();
    }
}
