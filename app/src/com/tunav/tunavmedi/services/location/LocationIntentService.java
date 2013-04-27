package com.tunav.tunavmedi.services.location;

import java.util.HashMap;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

import com.tunav.tunavmedi.interfaces.LocationsHandler;

public class LocationIntentService extends IntentService {

    private static final String TAG = "LOCATION_INTENTSERVICE";
    private static final String PROXIMITY_ALERT = "PROXIMITY_ALERT.";
    HashMap<String, Location> locations = null;
    private LocationsHandler helper = null;

    public LocationIntentService(String name, LocationsHandler helper) {
	// Constractor
	super(name);
	this.helper = helper;
	updateLocations();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    private void setLocationProximityAlert(LocationManager locationManager,
	    Location location, String locationName, float radius,
	    long expiration) {
	Intent intent = new Intent(PROXIMITY_ALERT + locationName);
	intent.putExtra("locationName", locationName);
	PendingIntent proximityIntent = PendingIntent.getBroadcast(this, -1,
		intent, 0);
	locationManager.addProximityAlert(location.getLatitude(),
		location.getLongitude(), radius, expiration, proximityIntent);
    }

    private void updateLocations() {
	locations = helper.getLocations();
    }
}
