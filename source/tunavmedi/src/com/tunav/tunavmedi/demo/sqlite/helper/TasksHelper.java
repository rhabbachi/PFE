
package com.tunav.tunavmedi.demo.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.tunav.tunavmedi.abstraction.TasksHandler;
import com.tunav.tunavmedi.datatype.Task;
import com.tunav.tunavmedi.demo.sqlite.database.DemoSQLite;
import com.tunav.tunavmedi.demo.sqlite.database.TasksContract;

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
                int inserts = 3;
                Random random = new Random();
                for (int i = 0; i < inserts; i++) {
                    // TODO
                    ArrayList newTasks = pullTasks();
                    setChanged();
                    notifyObservers(newTasks);
                    Log.i(tag, "Observers notified");
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

    private String getStringFromAssets(String description_file) {
        String text = null;
        InputStream is;
        try {
            is = mContext.getResources().getAssets().open(description_file);
            byte[] buffer = new byte[is.available()];
            int red = is.read(buffer);
            is.close();
            if (red > 0) text = new String(buffer);
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
        SQLiteDatabase database;
        Cursor cursor;
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

            assert database != null;
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
                            .getColumnIndexOrThrow(TasksContract.KEY_ISURGENT)) != 0;
                    Long due = cursor.getLong(cursor
                            .getColumnIndexOrThrow(TasksContract.KEY_DUE));
                    boolean done = cursor.getInt(cursor
                            .getColumnIndexOrThrow(TasksContract.KEY_ISDONE)) != 0;
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
            cursor.close();
            database.close();
        } catch (SQLiteException tr) {
            // database problem
            Log.e(tag, "SQLiteException");
            Log.d(tag, "SQLiteException", tr);
        } catch (IllegalArgumentException tr) {
            // something wired happened in the cursor
            Log.e(tag, "IllegalArgumentException");
            Log.d(tag, "IllegalArgumentException", tr);
        }
        return tasks;
    }

    @Override
    public int pushTask(ArrayList<Task> updatedTasks) {
        Log.v(tag, "setStatus()");
        SQLiteDatabase database = null;
        ContentValues cv = null;
        int returnStatus = 0;
        try {
            database = mDemoSQLite.getWritableDatabase();
            cv = new ContentValues();
            // TODO
        } catch (SQLiteException tr) {
            // database problem
            Log.e(tag, "SQLiteException");
            Log.d(tag, "SQLiteException", tr);
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return returnStatus;
    }
}
