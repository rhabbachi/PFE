package com.tunav.tunavmedi.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;

import com.tunav.tunavmedi.helpers.TasksHelper;
import com.tunav.tunavmedi.interfaces.TasksHandler;

public class TasksProvider extends ContentProvider {
    private static final String TAG = "TASKS_PROVIDER";
    // Authority to match
    private static final String AUTHORITY = "com.tunav.provider.tunavmedi";
    private Object helper = null;
    private final String MIME_ITEM = "vnd.android.cursor.item/vnd.tunav.task";
    private final String MIME_DIR = "vnd.android.cursor.dir/vnd.tunav.tasks";

    // Distinguish multi-rows query from single row query
    private static final UriMatcher uriMatcher;
    private static final int MULTI_ROW = 1;
    private static final int SINGLE_ROW = 2;
    static {
	uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	uriMatcher.addURI(AUTHORITY, "tasks", MULTI_ROW);
	uriMatcher.addURI(AUTHORITY, "tasks/#", SINGLE_ROW);
    }

    public static final Uri CONTENT_URI = Uri
	    .parse("content://com.tunav.provider.tunavmedi/tasks");

    public TasksProvider(Object _helper) throws IllegalArgumentException {
	if (_helper instanceof TasksHandler) {
	    this.helper = _helper;
	} else {
	    String err = "Does not implement com.tunav.tunavmedi.interfaces.TasksHandler";
	    throw new IllegalArgumentException(err);
	}
    }

    public TasksProvider() {
	//FIXME
	Context context = null;
	String name = null;
	CursorFactory factory = null;
	int version = 0;

	this.helper = new TasksHelper(context, name, factory, version);
    }

    @Override
    public boolean onCreate() {
	// TODO
	return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
	    String[] selectionArgs, String sortOrder) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getType(Uri uri) {
	switch (uriMatcher.match(uri)) {
	case MULTI_ROW:
	    return MIME_DIR;
	case SINGLE_ROW:
	    return MIME_ITEM;
	default:
	    throw new IllegalArgumentException("Unsupported URI: " + uri);
	}
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
	    String[] selectionArgs) {
	// TODO Auto-generated method stub
	return 0;
    }
}
