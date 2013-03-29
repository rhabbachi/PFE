package com.tunav.tunavmedi.gui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.providers.TasksProvider;

public class MainActivity extends Activity {

    private ContentResolver contentResolver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	//set the global contentResolver
	contentResolver = getContentResolver();
    }

    @Override
    protected void onDestroy() {
	// TODO
	super.onDestroy();
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
	    // Backward compatible call
	    // Class c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?
	    // PreferencesActivity.class : FragmentPreferences.class;
	    Intent intent = new Intent(this, PreferencesActivity.class);
	    startActivityForResult(intent, SHOW_PREFERENCES);
	    return true;
	}
	}
	return false;
    }

    private void getLocation() {
    }

    private Cursor queryTasksAll() {	// FIXME
	Uri uri = TasksProvider.CONTENT_URI;
	// the minimum set of columns required to satisfy the requirements
	String[] result_columns = null;
	String selection = null;
	String[] selectionArgs = null;
	String sortOrder = null;
	
	return contentResolver.query(uri, result_columns, selection,
		selectionArgs, sortOrder);
    }
}