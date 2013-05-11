package com.tunav.tunavmedi.demo.sqlite.helper;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.tunav.tunavmedi.abstraction.TasksHandler;
import com.tunav.tunavmedi.datatype.Task;
import com.tunav.tunavmedi.datatype.Task.Priority;
import com.tunav.tunavmedi.datatype.Task.Status;
import com.tunav.tunavmedi.demo.sqlite.DemoSQLite;
import com.tunav.tunavmedi.demo.sqlite.contract.TasksContract;

public class TasksHelper extends TasksHandler {

	public static final String tag = "TaskHelper";

	private Context mContext = null;
	private DemoSQLite mDemoSQLite = null;

	public TasksHelper(Context context) {
		Log.v(tag, "TasksHelper()");
		mDemoSQLite = new DemoSQLite(context);
		mContext = context;
	}

	@Override
	public ArrayList<Task> getTasks() {
		Log.v(tag, "getTasks()");
		ArrayList<Task> tasks = new ArrayList<Task>();
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			database = mDemoSQLite.getReadableDatabase();

			String table = TasksContract.TABLE_NAME;
			String[] columns = { "*" };
			String selection = null;
			String[] selectionArgs = null;
			String groupBy = null;
			String having = null;
			String orderBy = null;

			cursor = database.query(//
					table,// The table to query
					columns,// The columns to return
					selection,// The columns for the WHERE clause
					selectionArgs,// The values for the WHERE clause
					groupBy,// don't group the rows
					having,// don't filter by row groups
					orderBy);// The sort order

			if (cursor.moveToFirst()) {
				do {
					Long taskID = cursor.getLong(cursor
							.getColumnIndexOrThrow(TasksContract._ID));
					String from = cursor.getString(cursor
							.getColumnIndexOrThrow(TasksContract.KEY_TITLE));
					Long created = cursor
							.getLong(cursor
									.getColumnIndexOrThrow(TasksContract.KEY_DATE_CREATION));
					String priority = cursor.getString(cursor
							.getColumnIndexOrThrow(TasksContract.KEY_PRIORITY));
					Long due = cursor.getLong(cursor
							.getColumnIndexOrThrow(TasksContract.KEY_DATE_DUE));
					String status = cursor.getString(cursor
							.getColumnIndexOrThrow(TasksContract.KEY_STATUS));
					String description = cursor
							.getString(cursor
									.getColumnIndexOrThrow(TasksContract.KEY_DESCRIPTION));
					Task task = new Task(taskID,//
							from,//
							new Timestamp(created),//
							description,//
							priority == "HIGH" ? Priority.PRIORITY_HIGH
									: Priority.PRIORITY_NORMAL,//
							status == "DONE" ? Status.STATUS_DONE
									: Status.STATUS_PROCEEDING,//
							new Date(due));
					// TODO some magic to get placemark instead of placemarkID
					tasks.add(task);
				} while (cursor.moveToNext());
			} else {
				Log.e(tag, "Cursor Empty!");
			}
		} catch (SQLiteException tr) {
			// database problem
			Log.e(tag, "SQLiteException");
			Log.d(tag, "SQLiteException", tr);
		} catch (IllegalArgumentException tr) {
			// something wired happened in the cursor
			Log.e(tag, "IllegalArgumentException");
			Log.d(tag, "IllegalArgumentException", tr);
		} finally {
			cursor.close();
			database.close();
		}
		return tasks;
	}

	@Override
	public void setStatus(Long id, Status status) {
		Log.v(tag, "setStatus()");
		SQLiteDatabase db = null;
		ContentValues cv = null;
		try {
			db = mDemoSQLite.getWritableDatabase();
			cv = new ContentValues();

			cv.put(TasksContract.KEY_STATUS, status.toString());
			db.update(TasksContract.TABLE_NAME,//
					cv,//
					TasksContract._ID + "=?",//
					new String[] { id.toString() });
		} catch (SQLiteException tr) {
			// database problem
			Log.e(tag, "SQLiteException");
			Log.d(tag, "SQLiteException", tr);
		} finally {
			db.close();
		}
	}

	@Override
	public void setPriority(Long id, Priority priority) {
		Log.v(tag, "setPriority()");
		SQLiteDatabase db = null;
		ContentValues cv = null;
		try {
			db = mDemoSQLite.getWritableDatabase();
			cv = new ContentValues();

			cv.put(TasksContract.KEY_PRIORITY, priority.toString());
			db.update(TasksContract.TABLE_NAME,//
					cv,//
					TasksContract._ID + "=?",//
					new String[] { id.toString() });
		} catch (SQLiteException tr) {
			// database problem
			Log.e(tag, "SQLiteException");
			Log.d(tag, "SQLiteException", tr);
		} finally {
			db.close();
		}
	}
}
