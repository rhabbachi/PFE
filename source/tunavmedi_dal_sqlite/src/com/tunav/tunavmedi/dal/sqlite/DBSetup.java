
package com.tunav.tunavmedi.dal.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tunav.tunavmedi.dal.sqlite.contract.CredentialsContract;
import com.tunav.tunavmedi.dal.sqlite.contract.PatientsContract;
import com.tunav.tunavmedi.dal.sqlite.contract.PlacemarksContract;

public class DBSetup extends SQLiteOpenHelper {

    private static final String tag = "DBSetup";

    private static final String DATABASE_NAME = "helpers";
    private static final int DATABASE_VERSION = 1;

    public DBSetup(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.v(tag, "DemoSQLite()");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(tag, "onCreate()");
        Log.i(tag, "Creating New database.");
        try {
            Log.v(tag, "executing:" + PlacemarksContract.SQL_CREATE_TABLE);
            db.execSQL(PlacemarksContract.SQL_CREATE_TABLE);
            Log.v(tag, "executing:" + PlacemarksContract.SQL_CREATE_INDEX_ID);
            db.execSQL(PlacemarksContract.SQL_CREATE_INDEX_ID);
            for (int i = 0; i < PlacemarksContract.DUMMIES_LOCATIONS_HOME.length; i++) {
                String sql = PlacemarksContract.getInsert(i);
                Log.v(tag, "executing: " + sql);
                db.execSQL(sql);
            }
            // Credentials
            Log.v(tag, "executing: " + CredentialsContract.SQL_CREATE_TABLE);
            db.execSQL(CredentialsContract.SQL_CREATE_TABLE);
            Log.v(tag, "executing: " + CredentialsContract.SQL_CREATE_INDEX_LOGIN);
            db.execSQL(CredentialsContract.SQL_CREATE_INDEX_LOGIN);
            Log.v(tag, "executing: " + CredentialsContract.SQL_INSERT_DUMMY);
            db.execSQL(CredentialsContract.SQL_INSERT_DUMMY);
            Log.v(tag, "executing: " + CredentialsContract.SQL_INSERT_DUMMY);

            // Patients
            Log.v(tag, "executing: " + PatientsContract.SQL_CREATE_TABLE);
            db.execSQL(PatientsContract.SQL_CREATE_TABLE);
            Log.v(tag, "executing: " + PatientsContract.SQL_CREATE_INDEX_ID);
            db.execSQL(PatientsContract.SQL_CREATE_INDEX_ID);

            for (String insert : PatientsContract.SQL_INSERT_DUMMIES) {
                Log.v(tag, insert);
                db.execSQL(insert);
            }
        } catch (SQLException tr) {
            Log.e(tag, "SQLException");
            Log.d(tag, "SQLException", tr);
        }
        Log.i(tag, "New database created.");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
            throws SQLException {
        // TODO dummy implementation
        Log.v(tag, "onDowngrade()");
        Log.i(tag, "Version mismatch, downgrading...");
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.v(tag, "onOpen()");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(tag, "Version mismatch, upgrading...");
        try {
            Log.v(tag, CredentialsContract.SQL_DROP_INDEX_LOGIN);
            db.execSQL(CredentialsContract.SQL_DROP_INDEX_LOGIN);
            Log.v(tag, CredentialsContract.SQL_DROP_TABLE);
            db.execSQL(CredentialsContract.SQL_DROP_TABLE);
            Log.v(tag, PatientsContract.SQL_DROP_INDEX_ID);
            db.execSQL(PatientsContract.SQL_DROP_INDEX_ID);
            Log.v(tag, PatientsContract.SQL_DROP_TABLE);
            db.execSQL(PatientsContract.SQL_DROP_TABLE);
            onCreate(db);
        } catch (SQLException e) {
            Log.e(tag, "SQLException");
            Log.d(tag, "SQLException", e);
        }
    }
}
