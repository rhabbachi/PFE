
package com.tunav.tunavmedi.dal.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.tunav.tunavmedi.dal.abstraction.PatientsHandler;
import com.tunav.tunavmedi.dal.datatype.Patient;
import com.tunav.tunavmedi.dal.sqlite.DBSetup;
import com.tunav.tunavmedi.dal.sqlite.contract.PatientsContract;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class PatientsHelper extends PatientsHandler {

    public static final String tag = "PatientsHelper";
    private Context mContext = null;
    private DBSetup mDBHelper = null;

    public PatientsHelper(Context context) {
        super();
        Log.v(tag, "TasksHelper()");
        mContext = context;
        mDBHelper = new DBSetup(mContext);
        setNotifyTask(new Runnable() {
            private static final String tag = "com.tunav.tunavmedi.adl.sqlite.notify";

            @Override
            public void run() {
                int inserts = 3;
                Random random = new Random();
                try {
                    Thread.sleep(random.nextInt(1000 * 60 * 15));
                } catch (InterruptedException tr) {
                    String msg = "InterruptedException";
                    Log.v(tag, msg);
                    Log.d(tag, msg, tr);
                }
                for (int i = 0; i < inserts; i++) {
                    // TODO
                    ArrayList<Patient> newPatientsList = pullPatients();
                    setChanged();
                    notifyObservers(newPatientsList);
                    Log.i(tag, "Observers notified");
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
            if (red > 0) {
                text = new String(buffer);
            }
        } catch (IOException tr) {
            String msg = "IOException";
            Log.e(tag, msg);
            Log.d(tag, msg, tr);
        }
        return text;
    }

    @Override
    public ArrayList<Patient> pullPatients() {
        Log.v(tag, "getTasks()");
        ArrayList<Patient> patients = new ArrayList<Patient>();
        SQLiteDatabase database;
        Cursor cursor;
        try {
            database = mDBHelper.getReadableDatabase();

            String table = PatientsContract.TABLE_NAME;
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
                    Long id = cursor.getLong(cursor
                            .getColumnIndexOrThrow(PatientsContract._ID));
                    String name = cursor.getString(cursor
                            .getColumnIndexOrThrow(PatientsContract.KEY_NAME));
                    Long interned = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(PatientsContract.KEY_INTERNED));
                    Long updated = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(PatientsContract.KEY_UPDATED));
                    boolean urgent = cursor.getInt(cursor
                            .getColumnIndexOrThrow(PatientsContract.KEY_ISURGENT)) == 0 ? false
                            : true;
                    String recordPath = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(PatientsContract.KEY_RECORD));

                    String description = null;
                    if (recordPath != null) {
                        description = getStringFromAssets(recordPath);
                    }
                    Patient patient = new Patient(id, name, interned, description, updated);
                    patient.setUrgent(urgent);
                    // TODO some magic to get placemark instead of placemarkID
                    patients.add(patient);
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
        return patients;
    }

    @Override
    public int pushPatients(ArrayList<Patient> updatedPatients) {
        Log.v(tag, "pushPatients()");
        SQLiteDatabase database = null;
        ContentValues values = null;
        int returnStatus = 0;
        try {
            database = mDBHelper.getWritableDatabase();
            values = new ContentValues();
            for (Patient patient : updatedPatients) {
                values.clear();

                values.put(PatientsContract.KEY_UPDATED, patient.getUpdated());
                values.put(PatientsContract.KEY_ISURGENT, patient.isUrgent());

                String selection = PatientsContract._ID + " LIKE ?";
                String[] selectionArgs = {
                        String.valueOf(patient.getId())
                };

                int affected = database.update(PatientsContract.TABLE_NAME, values, selection,
                        selectionArgs);
                Log.i(tag, "rows affected= " + affected);
                returnStatus++;
            }
        } catch (SQLiteException tr) {
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
