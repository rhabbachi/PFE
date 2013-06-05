
package com.tunav.tunavmedi.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.fragment.NotImplemetedListFragment;
import com.tunav.tunavmedi.fragment.PatientListFragment;
import com.tunav.tunavmedi.service.PatientsService;

public class MainActivity extends Activity {

    private enum TabEnum {
        TASK(R.string.tab_patient_text,
                "TaskListFragment")
        , DICT(R.string.tab_dict_text,
                "DictListFragment")
        , MAIL(R.string.tab_mail_text,
                "MailsListFragment")
        , CALL(R.string.tab_call_text,
                "Call")
        , CONTACTS(R.string.tab_contacts_text,
                "ContactsListFragment");

        private Integer textID = null;
        private String tag = null;

        private TabEnum(Integer textID, String tag) {
            this.textID = textID;
            this.tag = tag;
        }
    };

    public static class TabListener<T extends Fragment> implements
            ActionBar.TabListener {
        private static final String tag = "TabListener";

        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private final Bundle mArgs;
        private Fragment mFragment;

        public TabListener(Activity activity, String tag, Class<T> clz) {
            this(activity, tag, clz, null);
            Log.v(tag, "TabListener()");
        }

        public TabListener(Activity activity, String tag, Class<T> clz,
                Bundle args) {
            Log.v(tag, "TabListener()");
            mActivity = activity;
            mTag = tag;
            mClass = clz;
            mArgs = args;

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state. If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
            if (mFragment != null && !mFragment.isDetached()) {
                FragmentTransaction ft = mActivity.getFragmentManager()
                        .beginTransaction();
                ft.detach(mFragment);
                ft.commit();
            }
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            Log.v(tag, "onTabReselected()");
            Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            Log.v(tag, "onTabSelected()");
            if (mFragment == null) {
                mFragment = Fragment.instantiate(mActivity, mClass.getName(),
                        mArgs);
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                ft.attach(mFragment);
            }
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            Log.v(tag, "onTabUnselected()");
            if (mFragment != null) {
                ft.detach(mFragment);
            }
        }
    };

    private static final String tag = "MainActivity";

    private static long back_pressed;

    public static final int RC_LOGIN = 0;

    public static void about(Context context) {
        Log.v(tag, "about()");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.about_title).setMessage(R.string.about_message);
        AlertDialog aboutDialog = builder.create();
        aboutDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_LOGIN:
                switch (resultCode) {
                    case RESULT_CANCELED:
                        finish();
                        break;
                    case RESULT_OK:
                        setupActionBar();
                        break;
                    default:
                        break;
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Log.v(tag, "onBackPressed()");

        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Press again to exit!", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    // Called at the start of the full lifetime.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(tag, "onCreate()");
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPrefs = getSharedPreferences(
                getResources().getString(R.string.sp_user)
                , MODE_PRIVATE);
        boolean isLogged = sharedPrefs.getBoolean(
                getResources().getString(R.string.spkey_is_logged), false);
        if (!isLogged) {
            Intent loginIntent = new Intent(LoginActivity.ACTION_LOGIN);
            startActivityForResult(loginIntent, RC_LOGIN);
        }

        try {
            if (isLogged) {
                setupActionBar();
            }

            ActionBar actionBar = getActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            actionBar
                    .setLogo(getResources().getDrawable(R.drawable.doctor));
            actionBar.setTitle("Dr. Who");// TODO
            // TASK tab
            actionBar.addTab(actionBar
                    .newTab()
                    .setText(TabEnum.TASK.textID)
                    .setTabListener(
                            new TabListener<PatientListFragment>(this,
                                    TabEnum.TASK.tag,
                                    PatientListFragment.class)));
            // MAIL tab
            actionBar.addTab(actionBar
                    .newTab()
                    .setText(TabEnum.MAIL.textID)
                    .setTabListener(
                            new TabListener<NotImplemetedListFragment>(
                                    this, TabEnum.MAIL.tag,
                                    NotImplemetedListFragment.class)));
            // CALL tab
            actionBar.addTab(actionBar
                    .newTab()
                    .setText(TabEnum.CALL.textID)
                    .setTabListener(
                            new TabListener<NotImplemetedListFragment>(
                                    this, TabEnum.CALL.tag,
                                    NotImplemetedListFragment.class)));
            // CONTACT tab
            actionBar.addTab(actionBar
                    .newTab()
                    .setText(TabEnum.CONTACTS.textID)
                    .setTabListener(
                            new TabListener<NotImplemetedListFragment>(
                                    this, TabEnum.CONTACTS.tag,
                                    NotImplemetedListFragment.class)));
            // DICT tab
            actionBar.addTab(actionBar
                    .newTab()
                    .setText(TabEnum.DICT.textID)
                    .setTabListener(
                            new TabListener<NotImplemetedListFragment>(
                                    this, TabEnum.DICT.tag,
                                    NotImplemetedListFragment.class)));

            if (savedInstanceState != null) {
                // restor last selected tab
                actionBar.setSelectedNavigationItem(savedInstanceState
                        .getInt("tab", 0));
            }
            Intent patients = new Intent(this, PatientsService.class);
            startService(patients);
        } catch (ActivityNotFoundException e) {
            Log.d(tag, null, e);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(tag, "onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    // Sometimes called at the end of the full lifetime.
    @Override
    public void onDestroy() {
        // Clean up any resources including ending threads,
        // closing database connections etc.
        Log.v(tag, "onDestroy");

        Intent patients = new Intent(this, PatientsService.class);
        stopService(patients);

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(tag, "onOptionItemSelected()");
        Log.i(tag, "item selected: " + item.toString());
        switch (item.getItemId()) {
            case R.id.action_about:
                about(this);
                return true;
            case R.id.action_logout:
                Intent logout = new Intent(LoginActivity.ACTION_LOGIN);
                startActivityForResult(logout, RC_LOGIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Called at the end of the active lifetime.
    @Override
    public void onPause() {
        // Suspend UI updates, threads, or CPU intensive processes
        // that don't need to be updated when the Activity isn't
        // the active foreground Activity.
        Log.v(tag, "onPause()");
        super.onPause();
    }

    // Called before subsequent visible lifetimes
    // for an activity process.
    @Override
    public void onRestart() {
        Log.v(tag, "onRestart()");
        super.onRestart();
        // Load changes knowing that the Activity has already
        // been visible within this process.
    }

    // Called after onCreate has finished, use to restore UI state
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(tag, "onRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        // Will only be called if the Activity has been
        // killed by the system since it was last visible.
    }

    // Called at the start of the active lifetime.
    @Override
    public void onResume() {
        Log.v(tag, "onResume()");
        super.onResume();
        // Resume any paused UI updates, threads, or processes required
        // by the Activity but suspended when it was inactive.
    }

    // Called to save UI state changes at the
    // end of the active lifecycle.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate and
        // onRestoreInstanceState if the process is
        // killed and restarted by the run time.
        Log.v(tag, "onSaveInstanceState()");
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("tab", getActionBar()
                .getSelectedNavigationIndex());
    }

    // Called at the start of the visible lifetime.
    @Override
    public void onStart() {
        Log.v(tag, "onStart()");
        super.onStart();
        // Apply any required UI change now that the Activity is visible.
    }

    // Called at the end of the visible lifetime.
    @Override
    public void onStop() {
        // Suspend remaining UI updates, threads, or processing
        // that aren't required when the Activity isn't visible.
        // Persist all edits or state changes
        // as after this call the process is likely to be killed.
        Log.v(tag, "onStop()");

        super.onStop();
    }

    private void setupActionBar() {
        getActionBar()
                .setLogo(getResources().getDrawable(R.drawable.doctor));
        getActionBar().setTitle("Dr. Who");// TODO
    }
}
