package com.tunav.tunavmedi;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static Context context;
    private static byte[] authToken = null;

    @Override
    public void onCreate() {
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
