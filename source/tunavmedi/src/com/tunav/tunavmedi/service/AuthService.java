
package com.tunav.tunavmedi.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.tunav.tunavmedi.app.TunavMedi;
import com.tunav.tunavmedi.dal.sqlite.helper.AuthenticationHelper;

public class AuthService extends IntentService {
    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public AuthService getService() {
            // Return this instance of LocalService so clients can call public
            // methods
            return AuthService.this;
        }
    }

    private static final String tag = "AuthenticationIntentService";

    public static final String ACTION_LOGIN = "com.tunav.tunavmedi.action.authentication.LOGIN";
    public static final String ACTION_LOGOUT = "com.tunav.tunavmedi.action.authentication.LOGOUT";
    public static final String ACTION_CHECK = "com.tunav.tunavmedi.action.authentication.CHECK";

    public static final String EXTRA_USERNAME = "com.tunav.tunavmedi.action.authentication.USERNAME";
    public static final String EXTRA_PASSWORD = "com.tunav.tunavmedi.action.authentication.PASSWORD";
    public static final String EXTRA_CODE = "com.tunav.tunavmedi.action.authentication.AUTHENTICATED";

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    private AuthenticationHelper mHelper;

    public AuthService() {
        super(tag);
    }

    public boolean authenticate(String login, String password) {
        Integer userID = mHelper.login(login, password);
        if (userID != null) {
            Log.d(tag, "Authentication successful!");
            SharedPreferences sharedPrefs = getSharedPreferences(
                    TunavMedi.SP_USER_NAME, MODE_PRIVATE);
            SharedPreferences.Editor sharedPrefsEditor = sharedPrefs.edit();

            sharedPrefsEditor.putBoolean(
                    TunavMedi.SP_USER_KEY_ISLOGGED, true);
            sharedPrefsEditor.putInt(TunavMedi.SP_USER_KEY_USERID,
                    userID);
            sharedPrefsEditor.putString(TunavMedi.SP_USER_KEY_USERNAME,
                    login);
            sharedPrefsEditor.putString(TunavMedi.SP_USER_KEY_PASSWORD,
                    password);
            Log.d(tag, TunavMedi.SP_USER_KEY_ISLOGGED + "=" + "true"
                    + "\n" + TunavMedi.SP_USER_KEY_USERID + "="
                    + userID + "\n" + TunavMedi.SP_USER_KEY_USERNAME
                    + "=" + login + "\n" + TunavMedi.SP_USER_KEY_PASSWORD
                    + "=" + password);
            // this pref is critical so we need to commit it
            sharedPrefsEditor.commit();
            Log.d(tag, "SharedPreferences commited!");
            return true;
        }
        return false;
    }

    public void deauthenticate() {
        Log.i(tag, "deauthenticate()");

        mHelper.logout();
        SharedPreferences sharedPrefs = getSharedPreferences(
                TunavMedi.SP_USER_NAME, MODE_PRIVATE);
        Editor sharedPrefsEditor = sharedPrefs.edit();
        sharedPrefsEditor.remove(TunavMedi.SP_USER_KEY_ISLOGGED);
        Log.v(tag, TunavMedi.SP_USER_KEY_ISLOGGED + " removed.");
        sharedPrefsEditor.remove(TunavMedi.SP_USER_KEY_USERID);
        Log.v(tag, TunavMedi.SP_USER_KEY_USERID + " removed.");
        // critical shared preferences, commiting
        sharedPrefsEditor.commit();
        Log.i(tag, "SharedPreferences commited!");
    }

    public boolean isAuthenticated() {
        return mHelper.isLogged();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(tag, "onCreate()");
        mHelper = new AuthenticationHelper(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getAction().equals(ACTION_LOGIN)) {
            String id = intent.getStringExtra(EXTRA_USERNAME);
            String password = intent.getStringExtra(EXTRA_PASSWORD);
            if (id != null || password != null) {
            } else {

            }
        } else if (intent.getAction().equals(ACTION_LOGOUT)) {
            deauthenticate();
        } else if (intent.getAction().equals(ACTION_CHECK)) {
            isAuthenticated();
        }
    }
}
