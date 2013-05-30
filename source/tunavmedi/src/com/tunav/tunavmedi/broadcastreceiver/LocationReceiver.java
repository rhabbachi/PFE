
package com.tunav.tunavmedi.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.commonsware.cwac.locpoll.LocationPoller;
import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.adapter.PatientsAdapter;

public class LocationReceiver extends BroadcastReceiver {
    public static final String tag = "LocationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(tag, "onReceive()");
        String sp = context.getResources().getString(R.string.sp_patientlist);
        String spkeyLocationUpdate = context.getResources().getString(
                R.string.spkey_location_update);
        SharedPreferences.Editor spEditor = context.getSharedPreferences(sp,
                Context.MODE_PRIVATE).edit();

        Location loc = (Location)
                intent.getExtras().get(LocationPoller.EXTRA_LOCATION);
        String msg;

        if (loc != null) {
            PatientsAdapter.setLocation(loc);
            spEditor.putBoolean(spkeyLocationUpdate, true);
            Log.i(tag, loc.toString());
        } else {
            msg = intent.getStringExtra(LocationPoller.EXTRA_ERROR);
            Log.e(tag, msg);
            Location lastKnown = (Location) intent.getExtras().get(
                    LocationPoller.EXTRA_LASTKNOWN);
            if (lastKnown != null) {
                PatientsAdapter.setLocation(lastKnown);
                spEditor.putBoolean(spkeyLocationUpdate, true);
                Log.i(tag, lastKnown.toString());
            } else {
                spEditor.putBoolean(spkeyLocationUpdate, false);
                Log.e(tag, "Location Unknown!");
            }
        }
        spEditor.apply();
    }
}
