package com.tunav.tunavmedi.gui;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.helpers.AuthenticationFromDatabaseHelper;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

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

	setContentView(R.layout.activity_login);

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
	if (mAuthTask != null) {
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
	    focusView.requestFocus();
	} else {
	    // Show a progress spinner, and kick off a background task to
	    // perform the user login attempt.
	    mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
	    showProgress(true);
	    mAuthTask = new UserLoginTask();
	    mAuthTask.execute((Void) null);
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
	@Override
	protected Boolean doInBackground(Void... params) {
	    AuthenticationFromDatabaseHelper helper = new AuthenticationFromDatabaseHelper(
		    getApplicationContext());

	    byte[] base64password = null;
	    try {
		// Always when working with strings and the crypto classes be
		// sure to always specify the encoding you want the byte
		// representation in.
		base64password = Base64.encode(mPassword.getBytes("UTF-8"),
			Base64.DEFAULT);
	    } catch (UnsupportedEncodingException uee) {
		Log.d(TAG, "NoSuchAlgorithmException", uee);
	    }

	    byte[] md5password = null;
	    try {
		MessageDigest digest;
		// Hash used is md5.
		//TODO make this hash agnostic
		digest = MessageDigest.getInstance("MD5");
		digest.reset();
		digest.update(base64password);
		md5password = digest.digest();
	    } catch (NoSuchAlgorithmException nsae) {
		Log.d(TAG, "NoSuchAlgorithmException", nsae);
	    }

	    return helper.authenticate(mID, md5password);
	}

	@Override
	protected void onPostExecute(final Boolean success) {
	    mAuthTask = null;
	    showProgress(false);

	    if (success) {
		finish();
	    } else {
		mPasswordView
			.setError(getString(R.string.error_incorrect_id_password));
		mPasswordView.requestFocus();
	    }
	}

	@Override
	protected void onCancelled() {
	    mAuthTask = null;
	    showProgress(false);
	}
    }
}
