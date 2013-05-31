
package com.tunav.tunavmedi.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.app.TunavMedi;
import com.tunav.tunavmedi.fragment.NotImplemetedListFragment;
import com.tunav.tunavmedi.fragment.PatientListFragment;
import com.tunav.tunavmedi.service.AuthService;

public class MainActivity extends Activity {

    private enum RequestCode {
        LOGIN
    }

    private enum TabEnum {
        TASK(R.string.tab_todo_text, android.R.drawable.ic_menu_today,
                "TaskListFragment")//
        , DICT(R.string.tab_dict_text, android.R.drawable.ic_menu_zoom,
                "DictListFragment")//
        , MAIL(R.string.tab_mail_text, android.R.drawable.sym_action_email,
                "MailsListFragment")//
        , CALL(R.string.tab_call_text, android.R.drawable.ic_dialog_dialer,
                "Call")//
        , CONTACTS(R.string.tab_contacts_text, android.R.drawable.ic_menu_view,
                "ContactsListFragment");//

        private Integer textID = null;
        private Integer iconID = null;
        private String tag = null;

        private TabEnum(Integer textID, Integer iconID, String tag) {
            this.textID = textID;
            this.iconID = iconID;
            this.tag = tag;
        }
    };

    public static class TabListener<T extends Fragment> implements
            ActionBar.TabListener {
        private static final String TAG = "TabListener";

        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private final Bundle mArgs;
        private Fragment mFragment;

        public TabListener(Activity activity, String tag, Class<T> clz) {
            this(activity, tag, clz, null);
            Log.v(TAG, "TabListener()");
        }

        public TabListener(Activity activity, String tag, Class<T> clz,
                Bundle args) {
            Log.v(TAG, "TabListener()");
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
            Log.v(TAG, "onTabReselected()");
            Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            Log.v(TAG, "onTabSelected()");
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
            Log.v(TAG, "onTabUnselected()");
            if (mFragment != null) {
                ft.detach(mFragment);
            }
        }
    };

    private static final String TAG = "MainActivity";

    private static long back_pressed;

    private void about() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult()");
        if (requestCode == RequestCode.LOGIN.ordinal()) {
            switch (resultCode) {
                case (RESULT_OK):
                    onLogin();
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
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
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        try {
            SharedPreferences sharedPrefs = getSharedPreferences(
                    TunavMedi.SP_USER_NAME, Activity.MODE_PRIVATE);
            boolean isLogged = sharedPrefs.getBoolean(
                    TunavMedi.SP_USER_KEY_ISLOGGED, false);
            if (!isLogged) {
                onLogin();
            } else {

                // TODO bindService(service, conn, flags)

                ActionBar actionBar = getActionBar();
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

                actionBar
                        .setLogo(getResources().getDrawable(R.drawable.doctor));
                actionBar.setTitle("Dr. Who");// TODO
                // TASK tab
                actionBar.addTab(actionBar
                        .newTab()
                        .setText(TabEnum.TASK.textID)
                        .setIcon(TabEnum.TASK.iconID)
                        .setTabListener(
                                new TabListener<PatientListFragment>(this,
                                        TabEnum.TASK.tag,
                                        PatientListFragment.class)));
                // MAIL tab
                actionBar.addTab(actionBar
                        .newTab()
                        .setText(TabEnum.MAIL.textID)
                        .setIcon(TabEnum.MAIL.iconID)
                        .setTabListener(
                                new TabListener<NotImplemetedListFragment>(
                                        this, TabEnum.MAIL.tag,
                                        NotImplemetedListFragment.class)));
                // CALL tab
                actionBar.addTab(actionBar
                        .newTab()
                        .setText(TabEnum.CALL.textID)
                        .setIcon(TabEnum.CALL.iconID)
                        .setTabListener(
                                new TabListener<NotImplemetedListFragment>(
                                        this, TabEnum.CALL.tag,
                                        NotImplemetedListFragment.class)));
                // CONTACT tab
                actionBar.addTab(actionBar
                        .newTab()
                        .setText(TabEnum.CONTACTS.textID)
                        .setIcon(TabEnum.CONTACTS.iconID)
                        .setTabListener(
                                new TabListener<NotImplemetedListFragment>(
                                        this, TabEnum.CONTACTS.tag,
                                        NotImplemetedListFragment.class)));
                // DICT tab
                actionBar.addTab(actionBar
                        .newTab()
                        .setText(TabEnum.DICT.textID)
                        .setIcon(TabEnum.DICT.iconID)
                        .setTabListener(
                                new TabListener<NotImplemetedListFragment>(
                                        this, TabEnum.DICT.tag,
                                        NotImplemetedListFragment.class)));

                if (savedInstanceState != null) {
                    // restor last selected tab
                    actionBar.setSelectedNavigationItem(savedInstanceState
                            .getInt("tab", 0));
                }
            }
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, null, e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    // Sometimes called at the end of the full lifetime.
    @Override
    public void onDestroy() {
        // Clean up any resources including ending threads,
        // closing database connections etc.
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    private void onLogin() {
        Log.v(TAG, "onLogin()");
        Intent loginIntent = new Intent("com.tunav.tunavmedi.action.LOGIN");
        startActivityForResult(loginIntent, RequestCode.LOGIN.ordinal());
    }

    private void onLogout() {
        Log.v(TAG, "Logout()");
        // TODO authentication serviec logout
        Intent logout = new Intent(this, AuthService.class);
        logout.setAction(AuthService.ACTION_LOGOUT);
        startService(logout);
        onLogin();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionItemSelected()");
        switch (item.getItemId()) {
            case R.id.action_about:
                about();
                return true;
            case R.id.action_logout:
                onLogout();
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
        Log.i(TAG, "onPause()");
        super.onPause();
    }

    // Called before subsequent visible lifetimes
    // for an activity process.
    @Override
    public void onRestart() {
        Log.i(TAG, "onRestart()");
        super.onRestart();
        // Load changes knowing that the Activity has already
        // been visible within this process.
    }

    // Called after onCreate has finished, use to restore UI state
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "onRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        // Will only be called if the Activity has been
        // killed by the system since it was last visible.
    }

    // Called at the start of the active lifetime.
    @Override
    public void onResume() {
        Log.i(TAG, "onResume()");
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
        Log.i(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("tab", getActionBar()
                .getSelectedNavigationIndex());
    }

    // Called at the start of the visible lifetime.
    @Override
    public void onStart() {
        Log.i(TAG, "onStart()");
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
        Log.i(TAG, "onStop()");
        super.onStop();
    }
}
