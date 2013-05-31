
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

public class PatientOptions extends DialogFragment {
    public static final String tag = "PatientOptions";
    public static final String ARG_POSITION = "position";
    public static final String ARG_PHOTO = "photo";
    public static final String ARG_NAME = "name";
    public static final String ARG_NAME_DEFAULT = "N/A";
    public static final String DEFAULT_PHOTO_NAME = "hospital";
    public static final String ARG_INTERNED = "interned";
    public static final String ARG_OPTIONS = "options";

    public static PatientOptions newInstance(String name, String photo, boolean isUrgent) {
        PatientOptions patientOptions = new PatientOptions();

        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_PHOTO, photo);
        boolean[] options = {
                isUrgent,
        };
        args.putBooleanArray(ARG_OPTIONS, options);
        patientOptions.setArguments(args);

        return patientOptions;
    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:
                    Intent intent = new Intent();
                    intent.putExtra(ARG_POSITION, getArguments().getInt(ARG_POSITION));
                    intent.putExtra("isUrgent", choiceValues[0]);
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

        builder.setTitle(getArguments().getString(ARG_NAME, ARG_NAME_DEFAULT))
                .setMultiChoiceItems(R.array.options_patient
                        , choiceValues
                        , mOnMultiChoiceClickListener)
                .setPositiveButton(R.string.options_patient_ok, mOnClickListener)
                .setNegativeButton(R.string.options_patient_cancel, mOnClickListener);

        return builder.create();
    }
}
