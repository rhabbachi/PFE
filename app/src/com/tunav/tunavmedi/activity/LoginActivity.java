package com.tunav.tunavmedi.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.abstraction.AuthenticationHandler;
import com.tunav.tunavmedi.app.TunavMedi;
import com.tunav.tunavmedi.service.AuthenticationIntentService;
import com.tunav.tunavmedi.service.AuthenticationIntentService.LocalBinder;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */

public class LoginActivity extends Activity {

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	public static final String tag = "LoginActivity";
	private UserLoginTask loginTask = null;
	// Values for ID and password at the time of the login attempt.
	private String mID;
	private String mPassword;
	private LoginActivity mActivity = this;
	// UI references.
	private EditText mIDView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	private ServiceConnection mConnection = null;
	private AuthenticationHandler mHelper = null;
	private boolean mBound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(tag, "onCreate()");
		mConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// We've bound to LocalService, cast the IBinder and get
				// LocalService instance
				LocalBinder binder = (LocalBinder) service;
				mHelper = binder.getHandler(mActivity);
				mBound = true;
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				mBound = false;
			}
		};

		Intent loginIntent = new Intent(this, AuthenticationIntentService.class);
		// loginIntent.setAction(AuthenticationIntentService.ACTION_LOGIN);
		// loginIntent.putExtra(AuthenticationIntentService.EXTRA_USERNAME,
		// mID);
		// loginIntent.putExtra(AuthenticationIntentService.EXTRA_PASSWORD,
		// mPassword);

		try {
			if (!bindService(loginIntent, mConnection, Context.BIND_AUTO_CREATE)) {
				// TODO service not bound.
				Log.e(tag, "bindService() FAIL!");
			} else {
				Log.i(tag, "bindService() OK");
			}
		} catch (SecurityException tr) {
			Log.getStackTraceString(tr);
		}

		setContentView(R.layout.loginactivity);
		// Set up the login form.
		// TODO autofilled login ID ?
		mIDView = (EditText) findViewById(R.id.ID);
		mIDView.setText(mID);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	// Called after onCreate has finished, use to restore UI state
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
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
		super.onRestart();
		// Load changes knowing that the Activity has already
		// been visible within this process.
	}

	// Called at the start of the visible lifetime.
	@Override
	public void onStart() {
		super.onStart();
		// Apply any required UI change now that the Activity is visible.
	}

	// Called at the start of the active lifetime.
	@Override
	public void onResume() {
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
		super.onSaveInstanceState(savedInstanceState);
	}

	// Called at the end of the active lifetime.
	@Override
	public void onPause() {
		// Suspend UI updates, threads, or CPU intensive processes
		// that don't need to be updated when the Activity isn't
		// the active foreground Activity.
		super.onPause();
	}

	// Called at the end of the visible lifetime.
	@Override
	public void onStop() {
		// Suspend remaining UI updates, threads, or processing
		// that aren't required when the Activity isn't visible.
		// Persist all edits or state changes
		// as after this call the process is likely to be killed.
		super.onStop();
	}

	// Sometimes called at the end of the full lifetime.
	@Override
	public void onDestroy() {
		// Clean up any resources including ending threads,
		// closing database connections etc.
		unbindService(mConnection);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid ID, missing fields, etc.), the errors
	 * are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (loginTask != null) {
			return;
		}

		// Reset errors.
		mIDView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mID = mIDView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			Log.d(tag, "Not a valid password");
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		}
		// FIXME what to do about this?
		// else if (mPassword.length() < 4) {
		// mPasswordView.setError(getString(R.string.error_invalid_password));
		// focusView = mPasswordView;
		// cancel = true;
		// }

		// Check for a valid ID.
		if (TextUtils.isEmpty(mID)) {
			Log.d(tag, "Not a valid ID");
			mIDView.setError(getString(R.string.error_field_required));
			focusView = mIDView;
			cancel = true;
		}

		// we are not making the assumption of getting mail as an id
		// else if (!mID.contains("@")) {
		// mIDView.setError(getString(R.string.error_invalid_ID));
		// focusView = mIDView;
		// cancel = true;
		// }

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			Log.d(tag, "Error aquaring login/password.");
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			loginTask = new UserLoginTask();
			loginTask.execute();
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		private static final String tag = "UserLoginTask";

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		Integer userID = null;

		public UserLoginTask() {
			Log.v(tag, "UserLoginTask()");
		}

		// invoked on the UI thread
		@Override
		protected void onPreExecute() {
			Log.v(tag, "onPreExecute()");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Log.v(tag, "doInBackground()");
			userID = mHelper.authenticate(mID, mPassword);
			return userID == null ? false : true;
		}

		// invoked on the UI thread
		@Override
		protected void onPostExecute(final Boolean success) {
			loginTask = null;
			showProgress(false);

			if (success) {
				Log.d(tag, "Authentication successful!");
				SharedPreferences sharedPrefs = getSharedPreferences(
						TunavMedi.SHAREDPREFS_NAME, MODE_PRIVATE);
				SharedPreferences.Editor sharedPrefsEditor = sharedPrefs.edit();

				sharedPrefsEditor.putBoolean(
						TunavMedi.SHAREDPREFS_KEY_ISLOGGED, true);
				sharedPrefsEditor.putInt(TunavMedi.SHAREDPREFS_KEY_USERID,
						userID);
				sharedPrefsEditor.putString(TunavMedi.SHAREDPREFS_KEY_USERNAME,
						mID);
				sharedPrefsEditor.putString(TunavMedi.SHAREDPREFS_KEY_PASSWORD,
						mPassword);
				Log.d(tag, TunavMedi.SHAREDPREFS_KEY_ISLOGGED + "=" + "true"
						+ "\n" + TunavMedi.SHAREDPREFS_KEY_USERID + "="
						+ userID + "\n" + TunavMedi.SHAREDPREFS_KEY_USERNAME
						+ "=" + mID + "\n" + TunavMedi.SHAREDPREFS_KEY_PASSWORD
						+ "=" + mPassword);
				// this pref is critical so we need to commit it
				sharedPrefsEditor.commit();
				Log.d(tag, "SharedPreferences commited!");

				Intent mainActivity = new Intent(mActivity, MainActivity.class);
				startActivity(mainActivity);
				setResult(RESULT_OK);
				Log.d(tag, "RESULT_OK");
			} else {
				Log.d(tag, "Authentication faild!");
				mPasswordView
						.setError(getString(R.string.error_incorrect_id_password));
				mPasswordView.requestFocus();
			}
		}

		// invoked on the UI thread
		@Override
		protected void onCancelled() {
			loginTask = null;
			showProgress(false);
		}
	}
}
