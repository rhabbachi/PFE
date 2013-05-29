
package com.tunav.tunavmedi.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

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
    public static final String ACTION_LOGIN = "com.tunav.tunavmedi.action.authentication.AUTHENTICATE";
    public static final String ACTION_LOGOUT = "com.tunav.tunavmedi.action.authentication.DEAUTHENTICATE";

    public static final String ACTION_CHECK = "com.tunav.tunavmedi.action.authentication.CHECK";
    public static final String EXTRA_USERNAME = "com.tunav.tunavmedi.action.authentication.USERNAME";
    public static final String EXTRA_PASSWORD = "com.tunav.tunavmedi.action.authentication.PASSWORD";
    public static final String EXTRA_USERID = "com.tunav.tunavmedi.action.authentication.USERID";

    public static final String EXTRA_CODE = "com.tunav.tunavmedi.action.authentication.CODE";

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private AuthenticationHelper mHelper;

    public AuthService() {
        super(tag);
        Log.v(tag, "AuthenticationIntentService()");
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

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        mHelper = new AuthenticationHelper(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }
}
