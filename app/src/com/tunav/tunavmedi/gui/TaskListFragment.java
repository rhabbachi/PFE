package com.tunav.tunavmedi.gui;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.providers.TasksProvider;

public class TaskListFragment extends Fragment implements
	LoaderCallbacks<Cursor> {

    private CursorAdapter cursorAdapter = null;
    private ContentResolver contentResolver = null;;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	//XXX
	// Create, or inflate the Fragment’s UI, and return it.
	// If this Fragment has no UI then return null.
	return inflater.inflate(R.layout.fragment_tasks_list, container, false);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
	// TODO
	cursorAdapter.swapCursor(data);
	// This handler is not synchronized with the UI thread yet.
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
	// TODO
	cursorAdapter.swapCursor(null);
	// This handler is not synchronized with the UI thread yet.
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
	Uri queryUri = TasksProvider.CONTENT_URI;
	// the minimum set of columns required to satisfy the requirements
	String[] result_columns = null;
	String selection = null;
	String[] selectionArgs = null;
	String sortOrder = null;
	return new CursorLoader(getActivity(), queryUri, result_columns,
		selection, selectionArgs, sortOrder);
    }
}
