
package com.tunav.tunavmedi.demo.sqlite.contract;

import android.provider.BaseColumns;

public class PatientsContract implements BaseColumns {
    public static final String TABLE = "Patients";

    public static final String KEY_ID_TYPE = "INTEGER";
    public static final String KEY_ID_CONSTRAINTS = "NOT NULL PRIMARY KEY AUTOINCREMENT";
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_FIRSTNAME_TYPE = "TEXT";
    public static final String KEY_FIRSTNAME_CONS = "NOT NULL";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_LASTNAME_TYPE = "TEXT";
    public static final String KEY_LASTNAME_CONS = "NOT NULL";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_BIRTHDAY_TYPE = "TEXT";
    public static final String KEY_BIRTHDAY_CONS = "NOT NULL";
    public static final String KEY_PHOTO = "photo_uri";
    public static final String KEY_PHOTO_TYPE = "TEXT";
    public static final String KEY_INFO = "info";
    public static final String KEY_INFO_TYPE = "info";

    public static final String SQL_CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s ",
            TABLE)//
            + "("//
            + String.format(" %s %s %s", _ID, KEY_ID_TYPE, KEY_ID_CONSTRAINTS)//
            + ","
            + String.format(" %s %s %s", KEY_FIRSTNAME, KEY_FIRSTNAME_TYPE, KEY_FIRSTNAME_CONS)//
            + ","
            + String.format(" %s %s %s", KEY_LASTNAME, KEY_LASTNAME_TYPE, KEY_LASTNAME_CONS)//
            + ","
            + String.format(" %s %s %s", KEY_BIRTHDAY, KEY_BIRTHDAY_TYPE, KEY_BIRTHDAY_CONS)//
            + ","
            + String.format(" %s %s %s", KEY_PHOTO, KEY_PHOTO_TYPE, "")//
            + ","
            + String.format(" %s %s", KEY_INFO, KEY_ID_TYPE)//
            + ");";

    public static final String SQL_DROP_TABLE = String.format(
            "DROP TABLE IF EXISTS %s ;", TABLE);

    public static final String INDEX_ID = "index" + _ID;

    public static final String SQL_CREATE_INDEX_ID = String.format(
            "CREATE UNIQUE INDEX IF NOT EXISTS %s ON %s ( %s );", INDEX_ID, TABLE, _ID);

    public static final String SQL_DROP_INDEX_ID = String.format("DROP INDEX IF EXISTS %s ;",
            INDEX_ID);

    public static final String[] FIRSTNAME_DUMMIES = {
            "firstnameOne", "firstnameTwo", "firstnameThree"
    };
    public static final String[] LASTNAME_DUMMIES = {
            "lastnameOne", "lastnameTwo", "lastnameThree"
    };
    public static final String[] BIRTHDAY_DUMMIES = {
            "1/1/1911", "2/2/1922", "3/3/1933"
    };
    public static final String SQL_INSERT_DUMMY = String.format("INSERT INTO %s (%s, %s, %s)",
            TABLE, KEY_FIRSTNAME, KEY_LASTNAME, KEY_BIRTHDAY)//
            + String.format("VALUES (%s, %s, %s)", FIRSTNAME_DUMMIES[0], LASTNAME_DUMMIES[0],
                    BIRTHDAY_DUMMIES[0])//
            + String.format("VALUES (%s, %s, %s)", FIRSTNAME_DUMMIES[1], LASTNAME_DUMMIES[1],
                    BIRTHDAY_DUMMIES[1])//
            + String.format("VALUES (%s, %s, %s)", FIRSTNAME_DUMMIES[2], LASTNAME_DUMMIES[2],
                    BIRTHDAY_DUMMIES[2]);

}
