
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
    public static final String BDL_POSITION = "position";
    public static final String BDL_PHOTO = "photo";
    public static final String BDL_NAME = "name";
    public static final String BDL_NAME_DEFAULT = "N/A";
    public static final String DEFAULT_PHOTO_NAME = "hospital";
    public static final String BDL_URGENT = "isUrgent";

    public static PatientOptions newInstance(String name, String photo, boolean isUrgent) {
        PatientOptions patientOptions = new PatientOptions();

        Bundle args = new Bundle();
        args.putString(BDL_NAME, name);
        args.putString(BDL_PHOTO, photo);
        args.putBoolean(BDL_URGENT, isUrgent);
        patientOptions.setArguments(args);

        return patientOptions;
    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:
                    Intent intent = new Intent();
                    intent.putExtra(BDL_POSITION, getArguments().getInt(BDL_POSITION));
                    intent.putExtra(BDL_URGENT, isUrgent);
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
            switch (which) {
                case 0:
                    isUrgent = isChecked;
                    break;

                default:
                    break;
            }
        }
    };
    boolean isUrgent;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        isUrgent = getArguments().getBoolean(BDL_URGENT);
        boolean[] values = {
                isUrgent
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getArguments().getString(BDL_NAME, BDL_NAME_DEFAULT))
                .setMultiChoiceItems(R.array.options_patient
                        , values
                        , mOnMultiChoiceClickListener)
                .setPositiveButton(R.string.options_patient_ok, mOnClickListener)
                .setNegativeButton(R.string.options_patient_cancel, mOnClickListener);

        return builder.create();
    }
}
