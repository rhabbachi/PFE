
package com.tunav.tunavmedi.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.util.Log;

import com.tunav.tunavmedi.R;

public class ChargingReceiver extends BroadcastReceiver {
    public static final String tag = "ChargingReceiver";

    public static boolean isCharging(Context context) {
        Log.v(tag, "isCharging()");

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        return status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(tag, "ChargingReceiver.onReceive()");
        String spStatus = context.getResources().getString(R.string.sp_status);
        String spkeyPowerConnected = context.getResources().getString(
                R.string.spkey_power_connected);
        SharedPreferences sp = context.getSharedPreferences(spStatus, Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();
        String action = intent.getAction();
        Log.i(tag, action);
        if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
            spEditor.putBoolean(spkeyPowerConnected, true);
        } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            spEditor.putBoolean(spkeyPowerConnected, false);
        }
        spEditor.apply();
    }
}
