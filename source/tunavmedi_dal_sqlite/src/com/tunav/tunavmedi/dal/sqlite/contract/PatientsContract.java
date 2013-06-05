
package com.tunav.tunavmedi.dal.sqlite.contract;

import android.provider.BaseColumns;
import android.text.format.Time;

public abstract class PatientsContract implements BaseColumns {

    public static final String TABLE_NAME = "Patients";

    public static final String _ID_TYPE = "INTEGER";
    public static final String _ID_CONS = "PRIMARY KEY";

    public static final String KEY_INTERNED = "interned";
    public static final String KEY_INTERNED_TYPE = "INTEGER";
    public static final String KEY_INTERNED_CONS = "NOT NULL DEFAULT CURRENT_TIMESTAMP";

    public static final String KEY_UPDATED = "updated";
    public static final String KEY_UPDATED_TYPE = "INTEGER";
    public static final String KEY_UPDATED_CONS = String.format(
            "NOT NULL DEFAULT CURRENT_TIMESTAMP CHECK (%s >= %s)",
            KEY_UPDATED, KEY_INTERNED);

    public static final String KEY_NAME = "name";
    public static final String KEY_NAME_TYPE = "TEXT";
    public static final String KEY_NAME_CONS = "NOT NULL";

    public static final String KEY_ISURGENT = "is_urgent";
    public static final String KEY_ISURGENT_TYPE = "INTEGER";
    public static final String KEY_ISURGENT_CONS = "DEFAULT 0";

    public static final String KEY_RECORD = "record";
    public static final String KEY_RECORD_TYPE = "TEXT";

    public static final String KEY_PLACEMARK_ID = "placemark";
    public static final String KEY_PLACEMARK_ID_TYPE = "INTEGER";
    public static final String KEY_PLACEMARK_ID_CONS = "FOREIGN KEY REFERENCE "
            + PlacemarksContract.TABLE_NAME + "(" + PlacemarksContract._ID + ")";

    public static final String SQL_CREATE_TABLE = String
            .format("CREATE TABLE IF NOT EXISTS %s(", TABLE_NAME)//
            + String.format(" %s %s %s", _ID, _ID_TYPE, _ID_CONS)//
            + String.format(", %s %s %s", KEY_INTERNED, KEY_INTERNED_TYPE, KEY_INTERNED_CONS)//
            + String.format(", %s %s %s", KEY_UPDATED, KEY_UPDATED_TYPE, KEY_UPDATED_CONS)//
            + String.format(", %s %s %s", KEY_NAME, KEY_NAME_TYPE, KEY_NAME_CONS)//
            + String.format(", %s %s", KEY_RECORD, KEY_RECORD_TYPE)//
            + String.format(", %s %s %s", KEY_ISURGENT, KEY_ISURGENT_TYPE, KEY_ISURGENT_CONS)//
            + String.format(", %s %s", KEY_PLACEMARK_ID,
                    KEY_PLACEMARK_ID_TYPE)//
            + ");";

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
                            "(%s, %s, %s, %s, %s)"//
                            , KEY_INTERNED, KEY_UPDATED, KEY_NAME, KEY_RECORD, KEY_ISURGENT)
                    + String.format(
                            " VALUES ( %d , %d, '%s', '%s', %d)"//
                            , getTime(1, 5, 2013)
                            , getTime(1, 5, 2013)
                            , "Mr John Doe [A06/65]"
                            , "html/dossier1.html"
                            , 0),
            "INSERT INTO "
                    + TABLE_NAME
                    + String.format(
                            "(%s, %s, %s, %s, %s)"//
                            , KEY_INTERNED, KEY_UPDATED, KEY_NAME, KEY_RECORD, KEY_ISURGENT)
                    + String.format(
                            " VALUES ( %d , %d, '%s', '%s', %d)"//
                            , getTime(5, 5, 2013)
                            , getTime(5, 5, 2013)
                            , "Ms Jane Doe [B06/65]"
                            , "html/dossier1.html"
                            , 0),
            "INSERT INTO "
                    + TABLE_NAME
                    + String.format(
                            "(%s, %s, %s, %s, %s)"//
                            , KEY_INTERNED, KEY_UPDATED, KEY_NAME, KEY_RECORD, KEY_ISURGENT)
                    + String.format(
                            " VALUES ( %d , %d, '%s', '%s', %d)"//
                            , getTime(10, 5, 2013)
                            , getTime(10, 5, 2013)
                            , "Mr Flan Ben Foulan [C06/65]"
                            , "html/dossier1.html"
                            , 0),
            "INSERT INTO "
                    + TABLE_NAME
                    + String.format(
                            "(%s, %s, %s, %s, %s)"//
                            , KEY_INTERNED, KEY_UPDATED, KEY_NAME, KEY_RECORD, KEY_ISURGENT)
                    + String.format(
                            " VALUES ( %d , %d, '%s', '%s', %d)"//
                            , getTime(15, 5, 2013)
                            , getTime(15, 5, 2013)
                            , "Mme Foulana Bent Faltana [D06/65]"
                            , "html/dossier1.html"
                            , 0),
            "INSERT INTO "
                    + TABLE_NAME
                    + String.format(
                            "(%s, %s, %s, %s, %s)"//
                            , KEY_INTERNED, KEY_UPDATED, KEY_NAME, KEY_RECORD, KEY_ISURGENT)
                    + String.format(
                            " VALUES ( %d , %d, '%s', '%s', %d)"//
                            , getTime(20, 5, 2013)
                            , getTime(20, 5, 2013)
                            , "Mr Ammar Ben Zwer [E06/65]"
                            , "html/dossier1.html"
                            , 0)

    };

    public static Long currentTime() {
        Time time = new Time();
        time.setToNow();
        return time.toMillis(true);
    }

    public static Long getTime(int monthDay, int month, int year) {
        Time time = new Time();
        time.set(monthDay, month, year);
        return time.toMillis(true);
    }

    private PatientsContract() {
    }

}
