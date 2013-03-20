package com.tunav.tunavmedi.gui;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.R.layout;
import com.tunav.tunavmedi.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	static final private int MENU_PREFERENCES = Menu.FIRST + 1;
	private static final int SHOW_PREFERENCES = 1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case (MENU_PREFERENCES): {
//			Backward compatible call
//	        Class c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?    
//	                PreferencesActivity.class : FragmentPreferences.class;
			Intent intent = new Intent(this, PreferencesActivity.class);
			startActivityForResult(intent, SHOW_PREFERENCES);
			return true;
		}
		}
		return false;
	}
	
	private void getLocation(){
	}
}