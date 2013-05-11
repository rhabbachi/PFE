
package com.tunav.tunavmedi.demo.sqlite.contract;

import android.provider.BaseColumns;

public class RecordsContract implements BaseColumns {

    public static final String KEY_ID_TYPE = "INTEGER";
    public static final String KEY_ID_CONSTRAINTS = "NOT NULL PRIMARY KEY AUTOINCREMENT";
    public static final String KEY_INFO = "info";
    public static final String KEY_INFO_TYPE = "TEXT";
    public static final String KEY_INFO_CONSTRAINTS = "NOT NULL";
    public static final String KEY_UPDATED = "date_updated";
    public static final String KEY_UPDATED_TYPE = "INTEGER";
    public static final String KEY_UPDATED_CONSTRAINTS = "NOT NULL DEFAULT CURRENT_TIMESTAMP";
    public static final String KEY_PATIENT = "patient_id";
    public static final String KEY_PATIENT_TYPE = "INTEGER";
    public static final String KEY_PATIENT_CONSTRAINTS = "UNIQUE ON CONFLICT FAIL NOT NULL";
    public static final String TABLE = "Records";

    public static final String SQL_CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s ",
            TABLE)//
            + "("//
            + String.format(" %s %s %s", _ID, KEY_ID_TYPE, KEY_ID_CONSTRAINTS)//
            + ","
            + String.format(" %s %s %s", KEY_INFO, KEY_ID_TYPE, KEY_INFO_CONSTRAINTS)//
            + ","
            + String.format(" %s %s %s", KEY_UPDATED, KEY_UPDATED_TYPE, KEY_UPDATED_CONSTRAINTS)//
            + ","
            + String.format(" %s %s %s", KEY_INFO, KEY_ID_TYPE, KEY_INFO_CONSTRAINTS)//
            + ","
            + String.format(" %s %s %s", KEY_PATIENT, KEY_PATIENT_TYPE, KEY_PATIENT_CONSTRAINTS)//
            + ");";

    public static final String SQL_DROP_TABLE = String.format(
            "DROP TABLE IF EXISTS %s ;", TABLE);

    public static final String INDEX_ID = "index" + _ID;

    public static final String SQL_CREATE_INDEX_ID = String.format(
            "CREATE UNIQUE INDEX IF NOT EXISTS %s ON %s ( %s );", INDEX_ID, TABLE, _ID);

    public static final String SQL_DROP_INDEX_ID = String.format("DROP INDEX IF EXISTS %s ;",
            INDEX_ID);

    public static final String[] DUMMIES_INFO = {
            " Records info"
    };

    public static final String SQL_INSERT_DUMMY = String.format("INSERT INTO %s VALUES (%d, %s)",
            TABLE, 1, DUMMIES_INFO[1]);
}
