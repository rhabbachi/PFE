package com.tunav.tunavmedi.gui;

import java.util.List;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.R.xml;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferencesActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public void onBuildHeaders (List<Header> target){
	loadHeadersFromResource(R.xml.preferences_headers, target);
	}
}
