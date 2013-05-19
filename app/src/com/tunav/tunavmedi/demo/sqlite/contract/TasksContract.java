
package com.tunav.tunavmedi.demo.sqlite.contract;

import android.provider.BaseColumns;

import java.util.Calendar;

public abstract class TasksContract implements BaseColumns {

    public static final String TABLE_NAME = "Tasks";

    public static final String _ID_TYPE = "INTEGER";
    public static final String _ID_CONS = "PRIMARY KEY";
    public static final String KEY_CREATED = "created";
    public static final String KEY_CREATED_TYPE = "INTEGER";
    public static final String KEY_CREATED_CONS = "NOT NULL";
    public static final String KEY_UPDATED = "updated";
    public static final String KEY_UPDATED_TYPE = "INTEGER";
    public static final String KEY_UPDATED_CONS = String.format("NOT NULL CHECK ( %s >= %s )",
            KEY_UPDATED, KEY_CREATED);
    public static final String KEY_TITLE = "title";
    public static final String KEY_TITLE_TYPE = "TEXT";
    public static final String KEY_TITLE_CONS = "NOT NULL";
    public static final String KEY_ISURGENT = "is_urgent";
    public static final String KEY_ISURGENT_TYPE = "INTEGER";
    public static final String KEY_ISURGENT_CONS = "DEFAULT 0";
    public static final String KEY_DUE = "due";
    public static final String KEY_DUE_TYPE = "INTEGER";
    public static final String KEY_DUE_CONS = "DEFAULT NULL";
    public static final String KEY_ISDONE = "is_done";
    public static final String KEY_ISDONE_TYPE = "INTEGER";
    public static final String KEY_ISDONE_CONS = "DEFAULT 0";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DESCRIPTION_TYPE = "TEXT";
    public static final String KEY_PLACEMARK_ID = "placemark";
    public static final String KEY_PLACEMARK_ID_TYPE = "INTEGER";

    public static final String SQL_CREATE_TABLE = String
            .format("CREATE TABLE IF NOT EXISTS %s", TABLE_NAME)//
            + String.format("( %s %s %s", _ID, _ID_TYPE, _ID_CONS)//
            + String.format(", %s %s %s", KEY_CREATED, KEY_CREATED_TYPE, KEY_CREATED_CONS)//
            + String.format(", %s %s %s", KEY_UPDATED, KEY_UPDATED_TYPE, KEY_UPDATED_CONS)//
            + String.format(", %s %s %s", KEY_DUE, KEY_DUE_TYPE, KEY_DUE_CONS)//
            + String.format(", %s %s %s", KEY_TITLE, KEY_TITLE_TYPE, KEY_TITLE_CONS)//
            + String.format(", %s %s", KEY_DESCRIPTION, KEY_DESCRIPTION_TYPE)//
            + String.format(", %s %s %s", KEY_ISURGENT, KEY_ISURGENT_TYPE, KEY_ISURGENT_CONS)//
            + String.format(", %s %s %s", KEY_ISDONE, KEY_ISDONE_TYPE, KEY_ISDONE_CONS)//
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
                            , KEY_CREATED, KEY_UPDATED, KEY_TITLE, KEY_DESCRIPTION, KEY_ISURGENT,
                            KEY_ISDONE)
                    + String.format(
                            " VALUES ( %d , %d, %s, %s, %d, %d)"//
                            ,
                            Calendar.getInstance().getTime().getTime()
                            ,
                            Calendar.getInstance().getTime().getTime()
                            ,
                            "'Service de Pédiatrie - Addmission Patient [B06/65]'"
                            , "'html/dossier1.html'"
                            , 0
                            , 0),
            "INSERT INTO "
                    + TABLE_NAME
                    + String.format(
                            "(%s, %s, %s, %s, %s, %s)"//
                            , KEY_CREATED, KEY_UPDATED, KEY_TITLE, KEY_DESCRIPTION, KEY_ISURGENT,
                            KEY_ISDONE)
                    + String.format(
                            " VALUES ( %d , %d, %s, %s, %d, %d)"//
                            , Calendar.getInstance().getTime().getTime()
                            , Calendar.getInstance().getTime().getTime()
                            ,
                            "'Laboratoire de Biochimie - Résultats Analyse Biochimiques [65/B06]'"
                            , "'html/biochimique.html'"
                            , 0
                            , 0),
            "INSERT INTO "
                    + TABLE_NAME
                    + String.format(
                            "(%s, %s, %s, %s, %s, %s)"//
                            , KEY_CREATED, KEY_UPDATED, KEY_TITLE, KEY_DESCRIPTION, KEY_ISURGENT,
                            KEY_ISDONE)
                    + String.format(
                            " VALUES ( %d , %d, %s, %s, %d, %d)"//
                            ,
                            Calendar.getInstance().getTime().getTime()
                            ,
                            Calendar.getInstance().getTime().getTime()
                            ,
                            "'Laboratoire de Biochimie - Résultat de Dosage de HbA1c (hémoglobine glyquée) [65/B06]'"
                            , "'html/hba1c.html'"
                            , 0
                            , 0),
            "INSERT INTO "
                    + TABLE_NAME
                    + String.format(
                            "(%s, %s, %s, %s, %s, %s)"//
                            , KEY_CREATED, KEY_UPDATED, KEY_TITLE, KEY_DESCRIPTION, KEY_ISURGENT,
                            KEY_ISDONE)
                    + String.format(
                            " VALUES ( %d , %d, %s, %s, %d, %d)"//
                            ,
                            Calendar.getInstance().getTime().getTime()
                            ,
                            Calendar.getInstance().getTime().getTime()
                            ,
                            "'Laboratoire de Biochimie - Résultat Analyse Hormonales [65/B06]'"
                            , "'html/hormonales.html'"
                            , 0
                            , 0),
            "INSERT INTO "
                    + TABLE_NAME
                    + String.format(
                            "(%s, %s, %s, %s, %s, %s)"//
                            , KEY_CREATED, KEY_UPDATED, KEY_TITLE, KEY_DESCRIPTION, KEY_ISURGENT,
                            KEY_ISDONE)
                    + String.format(
                            " VALUES ( %d , %d, %s, %s, %d, %d)"//
                            ,
                            Calendar.getInstance().getTime().getTime()
                            ,
                            Calendar.getInstance().getTime().getTime()
                            ,
                            "'Service de Radiologie - Compte-Rendu Examen Radiologique N8781/08'"
                            , "'html/radio.html'"
                            , 0
                            , 0)
    };

    private TasksContract() {
    }
}
