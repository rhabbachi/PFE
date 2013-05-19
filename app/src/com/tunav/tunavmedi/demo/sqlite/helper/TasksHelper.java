
package com.tunav.tunavmedi.demo.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.tunav.tunavmedi.abstraction.TasksHandler;
import com.tunav.tunavmedi.datatype.Task;
import com.tunav.tunavmedi.demo.sqlite.DemoSQLite;
import com.tunav.tunavmedi.demo.sqlite.contract.TasksContract;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Random;

public class TasksHelper extends TasksHandler {

    public static final String tag = "TaskHelper";

    private Context mContext = null;
    private DemoSQLite mDemoSQLite = null;

    public TasksHelper(Context context) {
        super();
        Log.v(tag, "TasksHelper()");
        mContext = context;
        mDemoSQLite = new DemoSQLite(mContext);
        setNotificationThread(new Runnable() {
            private static final String tag = "setNotificationThread";

            @Override
            public void run() {
                int inserts = 0;
                Random random = new Random();
                for (int i = 0; i < inserts; i++) {
                    // TODO
                    notifyTasksChangedListeners();
                    try {
                        Thread.sleep(random.nextInt(5000));
                    } catch (InterruptedException tr) {
                        String msg = "InterruptedException";
                        Log.v(tag, msg);
                        Log.d(tag, msg, tr);
                    }
                }
            }
        });
    }

    @Override
    public Runnable getNotificationRunnable() {
        return new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000 * 15);
                        Log.i(tag, "Tasks Helper Thread Running");
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private String getStringFromAssets(String description_file) {
        String text = null;
        InputStream is = null;
        try {
            is = mContext.getResources().getAssets().open(description_file);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            text = new String(buffer);
        } catch (IOException tr) {
            String msg = "IOException";
            Log.e(tag, msg);
            Log.d(tag, msg, tr);
        }
        return text;
    }

    @Override
    public ArrayList<Task> pullTasks() {
        Log.v(tag, "getTasks()");
        ArrayList<Task> tasks = new ArrayList<Task>();
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = mDemoSQLite.getReadableDatabase();

            String table = TasksContract.TABLE_NAME;
            String[] columns = {
                    "*"
            };
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
                                    .getColumnIndexOrThrow(TasksContract.KEY_CREATED));
                    boolean urgent = cursor.getInt(cursor
                            .getColumnIndexOrThrow(TasksContract.KEY_ISURGENT)) == 0 ? false : true;
                    Long due = cursor.getLong(cursor
                            .getColumnIndexOrThrow(TasksContract.KEY_DUE));
                    boolean done = cursor.getInt(cursor
                            .getColumnIndexOrThrow(TasksContract.KEY_ISDONE)) == 0 ? false : true;
                    String description_file = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(TasksContract.KEY_DESCRIPTION));
                    Long updated = cursor.getLong(cursor
                            .getColumnIndexOrThrow(TasksContract.KEY_UPDATED));

                    String description = null;
                    if (description_file != null) {
                        description = getStringFromAssets(description_file);
                    }
                    Task task = new Task(taskID,//
                            from,//
                            created,//
                            updated,//
                            description,//
                            urgent,//
                            done,//
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
    public boolean pushTask(Task updatedTask) {
        Log.v(tag, "setStatus()");
        SQLiteDatabase db = null;
        ContentValues cv = null;
        boolean returnStatus = true;
        try {
            db = mDemoSQLite.getWritableDatabase();
            cv = new ContentValues();
            // TODO
        } catch (SQLiteException tr) {
            // database problem
            Log.e(tag, "SQLiteException");
            Log.d(tag, "SQLiteException", tr);
        } finally {
            db.close();
        }
        return returnStatus;
    }
}
