package com.tunav.tunavmedi.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.TunavMedi;
import com.tunav.tunavmedi.fragment.NotImplemetedListFragment;
import com.tunav.tunavmedi.fragment.TaskListFragment;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	private Context thisContext = null;
	private MainActivity thisActivity = null;

	private enum RequestCode {
		LOGIN
	};

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

	// Called at the start of the full lifetime.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		thisContext = this;
		thisActivity = this;
		try {
			SharedPreferences sharedPrefs = getSharedPreferences(
					TunavMedi.SHAREDPREFS_NAME, Activity.MODE_PRIVATE);
			boolean isLogged = sharedPrefs.getBoolean(
					TunavMedi.SHAREDPREFS_KEY_ISLOGGED, false);
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
								new TabListener<TaskListFragment>(this,
										TabEnum.TASK.tag,
										TaskListFragment.class)));
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

	// Called before subsequent visible lifetimes
	// for an activity process.
	@Override
	public void onRestart() {
		Log.i(TAG, "onRestart()");
		super.onRestart();
		// Load changes knowing that the Activity has already
		// been visible within this process.
	}

	// Called at the start of the visible lifetime.
	@Override
	public void onStart() {
		Log.i(TAG, "onStart()");
		super.onStart();
		// Apply any required UI change now that the Activity is visible.
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

	// Called at the end of the active lifetime.
	@Override
	public void onPause() {
		// Suspend UI updates, threads, or CPU intensive processes
		// that don't need to be updated when the Activity isn't
		// the active foreground Activity.
		Log.i(TAG, "onPause()");
		super.onPause();
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

	// Sometimes called at the end of the full lifetime.
	@Override
	public void onDestroy() {
		// Clean up any resources including ending threads,
		// closing database connections etc.
		Log.i(TAG, "onDestroy");
		clearUserData();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult()");
		if (requestCode == RequestCode.LOGIN.ordinal()) {
			switch (resultCode) {
			case (RESULT_OK):
				onLogin();
				break;
			case (RESULT_CANCELED):
				onQuit();
				break;
			case (RESULT_FIRST_USER):
				// TODO
				break;
			}
		}
	}

	private void onLogin() {
		Log.i(TAG, "onLogin()");
		Intent loginIntent = new Intent("com.tunav.tunavmedi.action.LOGIN");
		startActivityForResult(loginIntent, RequestCode.LOGIN.ordinal());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(TAG, "onCreateOptionsMenu()");
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
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
		case R.id.action_settings:
			showSettings();
			return true;
		case R.id.action_quit:
			onQuit();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void onQuit() {
		Log.i(TAG, "quit()");
		clearUserData();
		thisActivity.finish();
		moveTaskToBack(true);
	}

	private void showSettings() {
		// TODO Auto-generated method stub
	}

	private void clearUserData() {
		Log.i(TAG, "unLogin()");
		SharedPreferences sharedPrefs = getSharedPreferences(
				TunavMedi.SHAREDPREFS_NAME, MODE_PRIVATE);
		Editor sharedPrefsEditor = sharedPrefs.edit();
		sharedPrefsEditor.remove(TunavMedi.SHAREDPREFS_KEY_ISLOGGED);
		Log.v(TAG, TunavMedi.SHAREDPREFS_KEY_ISLOGGED + " removed.");
		sharedPrefsEditor.remove(TunavMedi.SHAREDPREFS_KEY_USERID);
		Log.v(TAG, TunavMedi.SHAREDPREFS_KEY_USERID + " removed.");
		// critical shared preferences, commiting
		sharedPrefsEditor.commit();
		Log.i(TAG, "SharedPreferences commited!");
	}

	private void onLogout() {
		Log.i(TAG, "Logout()");
		clearUserData();
		onLogin();
	}

	private void about() {
		// TODO Auto-generated method stub

	}

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

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			Log.v(TAG, "onTabUnselected()");
			if (mFragment != null) {
				ft.detach(mFragment);
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			Log.v(TAG, "onTabReselected()");
			Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT).show();
		}
	}
}