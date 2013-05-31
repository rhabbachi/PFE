
package com.tunav.tunavmedi.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.app.TunavMedi;
import com.tunav.tunavmedi.datatype.Patient;
import com.tunav.tunavmedi.fragment.dialog.PatientDisplay;
import com.tunav.tunavmedi.fragment.dialog.PatientOptions;
import com.tunav.tunavmedi.service.PatientsService;
import com.tunav.tunavmedi.service.PatientsService.SelfBinder;

public class PatientListFragment extends ListFragment implements ServiceConnection {

    private static final String tag = "PatientListFragment";

    private PatientsService mPatientService = null;
    private final OnItemLongClickListener mOnItemLongClickListener = new OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Log.v(tag, "onItemLongClick()");
            Log.i(tag, "Item clicked: " + id);

            Patient task = (Patient) parent.getItemAtPosition(position);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag(PatientDisplay.tag);

            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            // Create and show the dialog.
            assert task != null;
            PatientOptions taskOptions = PatientOptions.newInstance(task.getName(),
                    task.getPhoto(),
                    task.isUrgent(), task.alarmeOn());
            taskOptions.setTargetFragment(thisFragment, REQUESTCODE_TASKOPTIONS);
            ft.add(taskOptions, PatientOptions.tag);
            ft.commit();
            return true;
        }
    };

    public static final int REQUESTCODE_TASKDISPLAY = 0;
    public static final int REQUESTCODE_TASKOPTIONS = 1;
    private final Fragment thisFragment = this;

    private final OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.v(tag, "onListItemClick()");
            Log.i(tag, "Item clicked: " + id);

            Patient patient = (Patient) parent.getItemAtPosition(position);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag(PatientDisplay.tag);

            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            // Create and show the dialog.
            assert patient != null;
            PatientDisplay patientDisplay = PatientDisplay.newInstance(patient.getName(),
                    patient.getPhoto(),
                    patient.getRecord(), patient.getInTime());
            patientDisplay.setTargetFragment(thisFragment, REQUESTCODE_TASKDISPLAY);
            ft.add(patientDisplay, PatientDisplay.tag);
            ft.commit();
        }
    };

    private boolean isBound;

    private void doBindService() {
        if (mPatientService != null) {
            doUnBindService();
        }
        Intent service = new Intent(getActivity(), PatientsService.class);
        getActivity().bindService(service, this, Context.BIND_AUTO_CREATE);
    }

    private void doUnBindService() {
        if (mPatientService != null) {
            getActivity().unbindService(this);
            getActivity().stopService(new Intent(getActivity(), PatientsService.class));
        }
    }

    // Called once the parent Activity and the Fragment's UI have
    // been created.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(tag, "onActivityCreated()");
        // Complete the Fragment initialization Ð particularly anything
        // that requires the parent Activity to be initialized or the
        // Fragment's view to be fully inflated.
        getListView().setOnItemClickListener(mOnItemClickListener);
        getListView().setLongClickable(true);
        getListView().setOnItemLongClickListener(mOnItemLongClickListener);

        setEmptyText(getResources().getString(R.string.task_list_empty));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(tag, "onActivityResult()");

        switch (requestCode) {
            case REQUESTCODE_TASKOPTIONS:
                switch (resultCode) {
                    default:
                        break;
                    case Activity.RESULT_OK:
                        // TODO update task
                        break;
                }
                break;
            case REQUESTCODE_TASKDISPLAY:
                break;
            default:
                break;
        }
    }

    // Called when the Fragment is attached to its parent Activity.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v(tag, "onAttach()");
        // Get a reference to the parent Activity.
    }

    // Called to do the initial creation of the Fragment.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(tag, "onCreate()");
        // Initialize the Fragment.

        // for the action bar items
        setHasOptionsMenu(true);
        doBindService();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        Log.v(tag, "onCreateOptionsMenu()");
        menuInflater.inflate(R.menu.fragment_tasklist, menu);
    }

    // Called once the Fragment has been created in order for it to
    // create its user interface.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.v(tag, "onCreateView()");
        // Create, or inflate the Fragment's UI, and return it.
        // If this Fragment has no UI then return null.
        return inflater.inflate(android.R.layout.list_content, container,
                false);
    }

    // Called at the end of the full lifetime.
    @Override
    public void onDestroy() {
        Log.v(tag, "onDestroy()");
        // Clean up any resources including ending threads,
        // closing database connections etc.

        super.onDestroy();
    }

    // Called when the Fragment's View has been detached.
    @Override
    public void onDestroyView() {
        Log.v(tag, "onDestroyView()");
        // Clean up resources related to the View.
        super.onDestroyView();
    }

    // Called when the Fragment has been detached from its parent Activity.
    @Override
    public void onDetach() {
        Log.v(tag, "onDetach()");
        super.onDetach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(tag, "onOptionsItemSelected()");

        SharedPreferences.Editor spEditor = getActivity()
                .getSharedPreferences(TunavMedi.SP_USER_NAME,
                        Context.MODE_PRIVATE).edit();
        boolean state = item.isChecked();

        switch (item.getItemId()) {
            case R.id.tasklist_menu_done:
                spEditor.putBoolean(
                        getActivity().getResources().getString(
                                R.string.spkey_show_checked), state);
                spEditor.apply();
                if (state) {
                    item.setTitle(R.string.tasklist_menu_done_title_on);
                } else {
                    item.setTitle(R.string.tasklist_menu_done_title_off);
                }
                item.setChecked(!state);
                return true;
            case R.id.tasklist_menu_location:
                spEditor.putBoolean(
                        getActivity().getResources().getString(
                                R.string.spkey_sort_location), state);
                spEditor.apply();
                if (state) {
                    item.setTitle(R.string.tasklist_menu_location_title_on);
                } else {
                    item.setTitle(R.string.tasklist_menu_location_title_off);
                }
                item.setChecked(!state);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    // Called at the end of the active lifetime.
    @Override
    public void onPause() {
        Log.v(tag, "onPause()");
        // Suspend UI updates, threads, or CPU intensive processes
        // that don't need to be updated when the Activity isn't
        // the active foreground activity.
        // Persist all edits or state changes
        // as after this call the process is likely to be killed.

        doUnBindService();
        super.onPause();
    }

    // Called at the start of the active lifetime.
    @Override
    public void onResume() {
        super.onResume();
        Log.v(tag, "onResume()");
        // Resume any paused UI updates, threads, or processes required
        // by the Fragment but suspended when it became inactive.
        if (isBound) {
            setListAdapter(mPatientService.getAdapter());
            getListView().invalidateViews();
        } else {
            doBindService();
        }
    }

    // Called to save UI state changes at the
    // end of the active lifecycle.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.v(tag, "onSaveInstanceState()");
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate, onCreateView, and
        // onCreateView if the parent Activity is killed and restarted.
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.v(tag, "onServiceConnected()");
        SelfBinder binder = (SelfBinder) service;
        mPatientService = binder.getService();
        if (mPatientService != null) {
            isBound = true;
            setListAdapter(mPatientService.getAdapter());
            Log.i(tag, "binding succesful!");
        } else {
            isBound = false;
            Log.i(tag, "binding failed!");
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mPatientService = null;
        isBound = false;
    }

    // Called at the start of the visible lifetime.
    @Override
    public void onStart() {
        super.onStart();
        Log.v(tag, "onStart()");
        // Apply any required UI change now that the Fragment is visible.
    }

    // Called at the end of the visible lifetime.
    @Override
    public void onStop() {
        Log.v(tag, "onStop()");
        // Suspend remaining UI updates, threads, or processing
        // that aren't required when the Fragment isn't visible.
        super.onStop();
    }
}
