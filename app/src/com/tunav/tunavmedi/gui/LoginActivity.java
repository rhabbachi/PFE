package com.tunav.tunavmedi.gui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tunav.tunavmedi.MainActivity;
import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.TunavMedi;
import com.tunav.tunavmedi.helpers.sqlite.AuthenticationHelper;
import com.tunav.tunavmedi.interfaces.AuthenticationHandler;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    public static final String TAG = "LOGIN_ACTIVITY";
    private UserLoginTask loginTask = null;
    private Context thisContext = null;
    private LoginActivity thisActivity = null;
    // Values for ID and password at the time of the login attempt.
    private String mID;
    private String mPassword;

    // UI references.
    private EditText mIDView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private View mLoginStatusView;
    private TextView mLoginStatusMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	try {
	    setContentView(R.layout.activity_login);
	    thisContext = this;
	    thisActivity = this;
	    // Set up the login form.
	    // TODO autofilled login ID ?
	    mIDView = (EditText) findViewById(R.id.ID);
	    mIDView.setText(mID);

	    mPasswordView = (EditText) findViewById(R.id.password);
	    mPasswordView
		    .setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView,
				int id, KeyEvent keyEvent) {
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
	} catch (Exception e) {
	    Log.d(TAG, this.getClass().toString(), e);
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	super.onCreateOptionsMenu(menu);
	getMenuInflater().inflate(R.menu.login, menu);
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
	    Log.d(TAG, "Not a valid password");
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
	    Log.d(TAG, "Not a valid ID");
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
	    Log.d(TAG, "Error aquaring login/password.");
	    focusView.requestFocus();
	} else {
	    // Show a progress spinner, and kick off a background task to
	    // perform the user login attempt.
	    mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
	    showProgress(true);
	    loginTask = new UserLoginTask(new AuthenticationHelper(
		    getApplicationContext()));
	    loginTask.execute((Void) null);
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
	private static final String TAG = "USER_LOGIN_TASK";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	AuthenticationHandler helper = null;
	Integer userID = null;

	public UserLoginTask(AuthenticationHandler helper) {
	    Log.d(TAG, "login=" + mID + ", password=" + mPassword);
	    this.helper = helper;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
	    userID = helper.authenticate(mID, mPassword);
	    if (userID != null) {
		return true;
	    } else {
		return false;
	    }
	}

	@Override
	protected void onPostExecute(final Boolean success) {
	    loginTask = null;
	    showProgress(false);

	    if (success) {
		// TODO add user login to the shared preferences
		Log.d(TAG, "Authentication successful!");
		SharedPreferences sharedPrefs = thisActivity
			.getSharedPreferences(TunavMedi.SHAREDPREFS_NAME,
				MODE_PRIVATE);
		SharedPreferences.Editor sharedPrefsEditor = sharedPrefs.edit();

		sharedPrefsEditor.putBoolean(
			TunavMedi.SHAREDPREFS_KEY_ISLOGGED, true);
		Log.d(TAG, TunavMedi.SHAREDPREFS_KEY_ISLOGGED + "=true");

		sharedPrefsEditor.putInt(TunavMedi.SHAREDPREFS_KEY_USERID,
			userID);
		Log.d(TAG, TunavMedi.SHAREDPREFS_KEY_USERID + "=" + userID);

		// this pref is critical so we need to commit it
		sharedPrefsEditor.commit();
		Log.d(TAG, "SharedPreferences commited!");

		Intent mainActivity = new Intent(thisContext,
			MainActivity.class);
		startActivity(mainActivity);
		thisActivity.setResult(RESULT_OK);
		Log.d(TAG, "RESULT_OK");
	    } else {
		Log.d(TAG, "Authentication faild!");
		mPasswordView
			.setError(getString(R.string.error_incorrect_id_password));
		mPasswordView.requestFocus();
	    }
	}

	@Override
	protected void onCancelled() {
	    loginTask = null;
	    showProgress(false);
	}
    }
}
