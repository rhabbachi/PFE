
package com.tunav.tunavmedi.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.tunav.tunavmedi.R;

public class NetworkReceiver extends BroadcastReceiver {
    public static final String tag = "NetworkReceiver";

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = true;
        if (activeNetwork != null) {
            isConnected = activeNetwork.isConnected();
        }
        Log.i(tag, "isConnected: " + (isConnected ? "true" : "false"));
        return isConnected;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(tag, "onReceive()");
        String name = context.getResources().getString(R.string.sp_status);
        int mode = Context.MODE_PRIVATE;
        SharedPreferences.Editor spEditor = context.getSharedPreferences(name, mode).edit();

        boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,
                false);

        String key = context.getString(R.string.spkey_is_connected);
        spEditor.putBoolean(key, noConnectivity);

        String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);

        Log.i(tag, "noConnectivity: " + (noConnectivity ? "true" : "false"));
        Log.i(tag, "reason: " + reason);

        spEditor.apply();
        // boolean isFailover =
        // intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);
        //
        // NetworkInfo currentNetworkInfo = (NetworkInfo) intent
        // .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        // NetworkInfo otherNetworkInfo = (NetworkInfo) intent
        // .getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

    }

}
