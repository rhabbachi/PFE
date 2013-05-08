package com.tunav.tunavmedi.demo.sqlite.contract;

import java.util.Calendar;

import android.provider.BaseColumns;

public abstract class TasksContract implements BaseColumns {

	public static final String TABLE_NAME = "Tasks";

	public static final String KEY_ID_TYPE = "INTEGER";
	public static final String KEY_DATE_CREATION = "date_creation";
	public static final String KEY_TITLE = "title";
	public static final String KEY_TITLE_TYPE = "TEXT";
	public static final String KEY_DATE_CREATION_TYPE = "TIMESTAMPS";
	public static final String KEY_PRIORITY = "priority";
	public static final String KEY_PRIORITY_TYPE = "TEXT";
	public static final String KEY_PRIORITY_DEFAULT = "NORMAL";
	public static final String KEY_DATE_DUE = "date_due";
	public static final String KEY_DATE_DUE_TYPE = "DATETIME";
	public static final String KEY_DATE_DUE_DEFAULT = "NULL";
	public static final String KEY_STATUS = "status";
	public static final String KEY_STATUS_TYPE = "TEXT";
	public static final String KEY_STATUS_DEFAULT = "PROCEEDING";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_DESCRIPTION_TYPE = "TEXT";
	public static final String KEY_PLACEMARK_ID = "placemark";
	public static final String KEY_PLACEMARK_ID_TYPE = "INTEGER";

	public static final String SQL_CREATE_TABLE = String
			.format("CREATE TABLE IF NOT EXISTS %s ( %s %s NOT NULL PRIMARY KEY AUTOINCREMENT,%s %s NOT NULL, %s %s NOT NULL, %s %s DEFAULT %s, %s %s DEFAULT NULL, %s %s DEFAULT %s, %s %s, %s %s);",
					TABLE_NAME,//
					_ID, KEY_ID_TYPE,//
					KEY_TITLE, KEY_TITLE_TYPE,//
					KEY_DATE_CREATION, KEY_DATE_CREATION_TYPE,//
					KEY_PRIORITY, KEY_PRIORITY_TYPE, KEY_PRIORITY_DEFAULT,//
					KEY_DATE_DUE, KEY_DATE_DUE_TYPE,//
					KEY_STATUS, KEY_STATUS_TYPE, KEY_STATUS_DEFAULT,//
					KEY_DESCRIPTION, KEY_DESCRIPTION_TYPE,//
					KEY_PLACEMARK_ID, KEY_PLACEMARK_ID_TYPE);

	public static final String SQL_DROP_TABLE = String.format(
			"DROP TABLE IF EXISTS %s ;", TABLE_NAME);

	public static final String INDEX_ID = "index" + _ID;

	public static final String SQL_CREATE_INDEX_ID = "CREATE UNIQUE INDEX IF NOT EXISTS "
			+ INDEX_ID + " ON " + TABLE_NAME + " ( " + _ID + " ) " + " ;";

	public static final String SQL_DROP_INDEX_ID = "DROP INDEX IF EXISTS "
			+ INDEX_ID + " ;";

	public static final String SQL_INSERT_DUMMY = "INSERT INTO " + TABLE_NAME
			+ " VALUES ( 1, 'Radiology', "
			+ Calendar.getInstance().getTime().getTime()
			+ ",'NORMAL', NULL, 'PROCEEDING', 'Radios are ready', NULL);";

	private TasksContract() {
	}
}
