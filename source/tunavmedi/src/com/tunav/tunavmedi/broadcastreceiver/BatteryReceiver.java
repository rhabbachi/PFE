
package com.tunav.tunavmedi.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

public class BatteryReceiver extends BroadcastReceiver {
    public static final String tag = "BatteryReceiver";
    public static final Integer BATTERY_LEVEL_LOW = 15;

    public static float getBatteryLevel(Context context) {
        Log.v(tag, "getBatteryLevel()");

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return (level / (float) scale);
    }

    public static boolean getBatteryOk(Context context) {
        return getBatteryLevel(context) > BATTERY_LEVEL_LOW;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(tag, "batteryStatus.onReceive()");
        String action = intent.getAction();
        Log.i(tag, action);
        if (action.equals(Intent.ACTION_BATTERY_LOW)) {
            // batteryOkay = false; TODO
        } else if (action.equals(Intent.ACTION_BATTERY_OKAY)) {
            // batteryOkay = true; TODO
        }
        // onConfigure(); TODO
    }
}
