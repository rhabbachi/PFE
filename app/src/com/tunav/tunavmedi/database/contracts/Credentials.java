package com.tunav.tunavmedi.database.contracts;

import android.provider.BaseColumns;

import com.tunav.tunavmedi.database.SQL;

public abstract class Credentials extends SQL implements BaseColumns {
    public static final String DATABASE_NAME = "helpers";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "Credentials";
    public static final String KEY_ID_TYPE = "INTEGER";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_LOGIN_TYPE = "TEXT";
    public static final String KEY_PASSHASH = "passHash";
    public static final String KEY_PASSHASH_TYPE = "CHAR(32)";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
	    + TABLE_NAME//
	    + PARENTHESES_OPEN//
	    + _ID + SPACE
	    + KEY_ID_TYPE
	    + SPACE
	    + "NOT NULL PRIMARY KEY AUTOINCREMENT"//
	    + COMMA + KEY_LOGIN + SPACE + KEY_LOGIN_TYPE
	    + SPACE
	    + "UNIQUE NOT NULL"//
	    + COMMA + KEY_PASSHASH + SPACE + KEY_PASSHASH_TYPE + SPACE
	    + "UNIQUE NOT NULL"//
	    + PARENTHESES_CLOSE//
	    + SEMICOLLON;// FIXME

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "
	    + TABLE_NAME + SEMICOLLON;

    public static final String INDEX_LOGIN = "index_" + KEY_LOGIN;
    public static final String SQL_CREATE_INDEX_LOGIN = "CREATE UNIQUE INDEX IF NOT EXISTS "
	    + INDEX_LOGIN
	    + " ON "
	    + TABLE_NAME
	    + PARENTHESES_OPEN
	    + KEY_LOGIN
	    + PARENTHESES_CLOSE + SEMICOLLON;
    public static final String SQL_DROP_INDEX_LOGIN = "DROP INDEX IF EXISTS "
	    + INDEX_LOGIN + SEMICOLLON;
    public static final String SQL_INSERT_DUMMY = "INSERT INTO Credentials VALUES (1, 'login', 'password');";

    public Credentials() {

    }
}
