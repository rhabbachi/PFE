package com.tunav.tunavmedi.dal.sqlite.contract;

import android.provider.BaseColumns;

public abstract class CredentialsContract implements BaseColumns {

    public static final String TABLE_NAME = "Credentials";

    public static final String KEY_ID_TYPE = "INTEGER";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_LOGIN_TYPE = "TEXT";
    public static final String KEY_PASSHASH = "passHash";
    public static final String KEY_PASSHASH_TYPE = "CHAR(32)";

    public static final String SQL_CREATE_TABLE = String
            .format("CREATE TABLE IF NOT EXISTS %s ( %s %s NOT NULL PRIMARY KEY AUTOINCREMENT, %s %s UNIQUE NOT NULL, %s %s UNIQUE NOT NULL); ",
                    TABLE_NAME,//
                    _ID,//
                    KEY_ID_TYPE,//
                    KEY_LOGIN,//
                    KEY_LOGIN_TYPE,//
                    KEY_PASSHASH,//
                    KEY_PASSHASH_TYPE);

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "
            + TABLE_NAME + ";";

    public static final String INDEX_LOGIN = "index_" + KEY_LOGIN;
    public static final String SQL_CREATE_INDEX_LOGIN = "CREATE UNIQUE INDEX IF NOT EXISTS "
            + INDEX_LOGIN
            + " ON "
            + TABLE_NAME
            + " ( "
            + KEY_LOGIN
            + " ) "
            + " ;";
    public static final String SQL_DROP_INDEX_LOGIN = "DROP INDEX IF EXISTS "
            + INDEX_LOGIN + " ;";
    public static final String SQL_INSERT_DUMMY = "INSERT INTO Credentials VALUES (1, 'login', 'password');";

    public CredentialsContract() {

    }
}