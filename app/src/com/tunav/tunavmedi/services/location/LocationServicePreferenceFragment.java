package com.tunav.tunavmedi.services.location;

import com.tunav.tunavmedi.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class LocationServicePreferenceFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.location_service_preferences);
	}
}
