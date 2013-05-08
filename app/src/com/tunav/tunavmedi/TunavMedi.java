package com.tunav.tunavmedi;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class TunavMedi extends Application {

    private static String TAG = "APP";
    private static Context context;
    private static byte[] authToken = null;
    // DEV MODE
    public static final boolean DEVELOPER_MODE = true;
    // Shared Preferences
    public static final String SHAREDPREFS_NAME = "TUNAVMEDI_PREFS";
    public static final String SHAREDPREFS_KEY_ISLOGGED = "isLogged";
    public static final String SHAREDPREFS_KEY_USERID = "userID";
    public static final String SHAREDPREFS_KEY_USERNAME = "login";
    public static final String SHAREDPREFS_KEY_PASSWORD = "password";

    @Override
    public void onCreate() {
	Log.d(TAG, this.toString());
	super.onCreate();
	context = this;
    }

    public static Context getContext() {
	return context;
    }

    public static void setAuthToken(byte[] token) {
	authToken = token;
    }

    public static byte[] getAuthToken() {
	return authToken;
    }
}
