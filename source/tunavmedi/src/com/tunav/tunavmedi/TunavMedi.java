
package com.tunav.tunavmedi;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class TunavMedi extends Application {

    private static String tag = "Tunavmedi";
    private static Context context;
    // DEV MODE
    public static final boolean DEVELOPER_MODE = true;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        Log.d(tag, this.toString());
        super.onCreate();
        context = this;
    }
}
