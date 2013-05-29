
package com.tunav.tunavmedi.dal.sqlite.contract;

import android.provider.BaseColumns;

import java.util.Calendar;

public abstract class PatientsContract implements BaseColumns {

    public static final String TABLE_NAME = "Tasks";

    public static final String _ID_TYPE = "INTEGER";
    public static final String _ID_CONS = "PRIMARY KEY";
    public static final String KEY_INTERNED = "created";
    public static final String KEY_INTERNED_TYPE = "INTEGER";
    public static final String KEY_INTERNED_CONS = "NOT NULL";
    public static final String KEY_LASTUPDATED = "last_update";
    public static final String KEY_UPDATED_TYPE = "INTEGER";
    public static final String KEY_UPDATED_CONS = String.format("NOT NULL CHECK ( %s >= %s )",
            KEY_LASTUPDATED, KEY_INTERNED);
    public static final String KEY_NAME = "name";
    public static final String KEY_NAME_TYPE = "TEXT";
    public static final String KEY_NAME_CONS = "NOT NULL";
    public static final String KEY_ISURGENT = "is_urgent";
    public static final String KEY_ISURGENT_TYPE = "INTEGER";
    public static final String KEY_ISURGENT_CONS = "DEFAULT 0";
    public static final String KEY_ALARME = "alarme";
    public static final String KEY_ALARME_TYPE = "INTEGER";
    public static final String KEY_ALARME_CONS = "DEFAULT NULL";
    public static final String KEY_ALARMEON = "alarme_on";
    public static final String KEY_ALARMEON_TYPE = "INTEGER";
    public static final String KEY_ALARMEON_CONS = "DEFAULT 0";
    public static final String KEY_RECORD = "record";
    public static final String KEY_DESCRIPTION_TYPE = "TEXT";
    public static final String KEY_PLACEMARK_ID = "placemark";
    public static final String KEY_PLACEMARK_ID_TYPE = "INTEGER";

    public static final String SQL_CREATE_TABLE = String
            .format("CREATE TABLE IF NOT EXISTS %s", TABLE_NAME)//
            + String.format("( %s %s %s", _ID, _ID_TYPE, _ID_CONS)//
            + String.format(", %s %s %s", KEY_INTERNED, KEY_INTERNED_TYPE, KEY_INTERNED_CONS)//
            + String.format(", %s %s %s", KEY_LASTUPDATED, KEY_UPDATED_TYPE, KEY_UPDATED_CONS)//
            + String.format(", %s %s %s", KEY_ALARME, KEY_ALARME_TYPE, KEY_ALARME_CONS)//
            + String.format(", %s %s %s", KEY_NAME, KEY_NAME_TYPE, KEY_NAME_CONS)//
            + String.format(", %s %s", KEY_RECORD, KEY_DESCRIPTION_TYPE)//
            + String.format(", %s %s %s", KEY_ISURGENT, KEY_ISURGENT_TYPE, KEY_ISURGENT_CONS)//
            + String.format(", %s %s %s", KEY_ALARMEON, KEY_ALARMEON_TYPE, KEY_ALARMEON_CONS)//
            + String.format(", %s %s);", KEY_PLACEMARK_ID, KEY_PLACEMARK_ID_TYPE);

    public static final String SQL_DROP_TABLE = String.format(
            "DROP TABLE IF EXISTS %s ;", TABLE_NAME);

    public static final String INDEX_ID = "index" + _ID;

    public static final String SQL_CREATE_INDEX_ID = "CREATE UNIQUE INDEX IF NOT EXISTS "
            + INDEX_ID + " ON " + TABLE_NAME + " ( " + _ID + " ) " + " ;";

    public static final String SQL_DROP_INDEX_ID = "DROP INDEX IF EXISTS "
            + INDEX_ID + " ;";

    public static final String[] SQL_INSERT_DUMMIES = {
            "INSERT INTO "
                    + TABLE_NAME
                    + String.format(
                            "(%s, %s, %s, %s, %s, %s)"//
                            , KEY_INTERNED, KEY_LASTUPDATED, KEY_NAME, KEY_RECORD, KEY_ISURGENT,
                            KEY_ALARMEON)
                    + String.format(
                            " VALUES ( %d , %d, %s, %s, %d, %d)"//
                            ,
                            Calendar.getInstance().getTime().getTime()
                            ,
                            Calendar.getInstance().getTime().getTime()
                            ,
                            "'Mr John Doe [B06/65]'"
                            , "'html/dossier1.html'"
                            , 0
                            , 0)
    };

    private PatientsContract() {
    }
}
