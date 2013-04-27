package com.tunav.tunavmedi.database.contracts;

import com.tunav.tunavmedi.database.SQL;

import android.provider.BaseColumns;

public abstract class Tasks extends SQL implements BaseColumns {
    public static final String KEY_ID_TYPE = "INT";
    public static final String KEY_DATE_CREATED = "created";
    public static final String KEY_DATE_CREATED_TYPE = "TIMESTAMPS";
    public static final String KEY_PRIORITY = "priority";
    public static final String KEY_PRIORITY_TYPE = "";
    public static final String KEY_DATE_DUE = "due";
    public static final String KEY_DATE_DUE_TYPE = "DATETIME";
    public static final String KEY_STATUS = "status";
    public static final String KEY_STATUS_TYPE = "";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_DESCRIPTION_TYPE = "TEXT";
    public static final String KEY_PLACEMARK_ID = "placemark";
    public static final String KEY_PLACEMARK_ID_TYPE = "INT";

    public static final String DATABASE_NAME = "helpers";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "Tasks";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
	    + TABLE_NAME//
	    + PARENTHESES_OPEN//
	    + _ID + SPACE + KEY_ID_TYPE + "NOT NULL PRIMARY KEY AUTOINCREMENT"//
	    + COMMA + KEY_DATE_CREATED + SPACE + KEY_DATE_CREATED_TYPE//
	    + COMMA + KEY_PRIORITY + SPACE + KEY_PLACEMARK_ID_TYPE//
	    + COMMA + KEY_PRIORITY + SPACE + KEY_PRIORITY_TYPE//
	    + COMMA + KEY_DATE_DUE + SPACE + KEY_DATE_DUE_TYPE//
	    + COMMA + KEY_STATUS + SPACE + KEY_STATUS_TYPE//
	    + COMMA + KEY_DESCRIPTION + SPACE + KEY_DESCRIPTION_TYPE//
	    + COMMA + KEY_PLACEMARK_ID + SPACE + KEY_PLACEMARK_ID_TYPE//
	    + PARENTHESES_CLOSE//
	    + SEMICOLLON;//

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "
	    + TABLE_NAME + SEMICOLLON;

    public static final String SQL_INSERT_DUMMY = "";

    private Tasks() {

    }
}
