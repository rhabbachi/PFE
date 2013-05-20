
package com.tunav.tunavmedi.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class StatusMonitor extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String tag = "ProfileMonitor";
        // TODO
        Log.v(tag, "onReceive()");
        String action = intent.getAction();
        Log.i(tag, action);
        String shared_prefs = context.getResources().getString(0);
        SharedPreferences.Editor SPeditor = context
                .getSharedPreferences("", Context.MODE_PRIVATE).edit();
        if (action.equals(Intent.ACTION_BATTERY_LOW)) {

        } else if (action.equals(Intent.ACTION_BATTERY_OKAY)) {

        } else if (action.equals(Intent.ACTION_POWER_CONNECTED)) {

        } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {

        }
    }

}
