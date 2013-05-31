
package com.tunav.tunavmedi.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.commonsware.cwac.locpoll.LocationPoller;

import java.util.ArrayList;

public class LocationReceiver extends BroadcastReceiver {
    public interface OnNewLocationListener {
        public abstract void onNewLocationReceived(Location location);
    }

    public static final String tag = "LocationReceiver";
    static ArrayList<OnNewLocationListener> arrOnNewLocationListener =
            new ArrayList<OnNewLocationListener>();

    public static void clearOnNewLocationListener(
            OnNewLocationListener listener) {
        arrOnNewLocationListener.remove(listener);
    }

    // This function is called after the new point received
    private static void OnNewLocationReceived(Location location) {
        // Check if the Listener was set, otherwise we'll get an Exception
        // when we try to call it
        if (arrOnNewLocationListener != null) {
            // Only trigger the event, when we have any listener
            for (int i = arrOnNewLocationListener.size() - 1; i >= 0; i--) {
                arrOnNewLocationListener.get(i).onNewLocationReceived(
                        location);
            }
        }
    }

    // Allows the user to set a OnNewLocationListener outside of this class
    // and react to the event.
    // A sample is provided in ActDocument.java in method: startStopTryGetPoint
    public static void setOnNewLocationListener(
            OnNewLocationListener listener) {
        arrOnNewLocationListener.add(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(tag, "onReceive()");

        Location loc = (Location)
                intent.getExtras().get(LocationPoller.EXTRA_LOCATION);
        String msg;

        if (loc != null) {
            OnNewLocationReceived(loc);
            Log.i(tag, loc.toString());
        } else {
            msg = intent.getStringExtra(LocationPoller.EXTRA_ERROR);
            Log.e(tag, msg);
            Location lastKnown = (Location) intent.getExtras().get(
                    LocationPoller.EXTRA_LASTKNOWN);
            if (lastKnown != null) {
                OnNewLocationReceived(lastKnown);
                Log.i(tag, lastKnown.toString());
            } else {
                Log.e(tag, "Location Unknown!");
            }
        }
    }
}
