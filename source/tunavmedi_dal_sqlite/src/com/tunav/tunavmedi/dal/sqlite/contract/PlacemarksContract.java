
package com.tunav.tunavmedi.dal.sqlite.contract;

import android.provider.BaseColumns;

public class PlacemarksContract implements BaseColumns {
    public static final String TABLE_NAME = "Placemarks";

    public static final String _ID_TYPE = "INTEGER";
    public static final String _ID_CONS = "PRIMARY KEY";

    public static final String KEY_NAME = "name";
    public static final String KEY_NAME_TYPE = "TEXT";
    public static final String KEY_NAME_CONS = "NOT NULL";

    public static final String KEY_MAP = "map";
    public static final String KEY_MAP_TYPE = "TEXT";
    public static final String KEY_MAP_CONS = "NOT NULL";

    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LONGITUDE_TYPE = "REAL";
    public static final String KEY_LONGITUDE_CONS = "NOT NULL";

    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LATITUDE_TYPE = "REAL";
    public static final String KEY_LATITUDE_CONS = "NOT NULL";

    public static final String KEY_ALTITUDE = "altitude";
    public static final String KEY_ALTITUDE_TYPE = "REAL";
    public static final String KEY_ALTITUDE_CONS = "NOT NULL";

    public static final String SQL_CREATE_TABLE = String
            .format("CREATE TABLE IF NOT EXISTS %s(", TABLE_NAME)//
            + String.format(" %s %s %s,", _ID, _ID_TYPE, _ID_CONS)//
            + String.format(" %s %s %s,", KEY_NAME, KEY_NAME_TYPE, KEY_MAP_CONS)//
            + String.format(" %s %s %s,", KEY_MAP, KEY_MAP_TYPE, KEY_MAP_CONS)//
            + String.format(" %s %s %s,", KEY_LONGITUDE, KEY_LONGITUDE_TYPE, KEY_LONGITUDE_CONS)//
            + String.format(" %s %s %s,", KEY_LATITUDE, KEY_LATITUDE_TYPE, KEY_LATITUDE_CONS)//
            + String.format(" %s %s %s", KEY_ALTITUDE, KEY_ALTITUDE_TYPE, KEY_ALTITUDE_CONS)//
            + ");";

    public static final String SQL_DROP_TABLE = String.format(
            "DROP TABLE IF EXISTS %s ;", TABLE_NAME);

    public static final String INDEX_ID = "index" + _ID;

    public static final String SQL_CREATE_INDEX_ID = "CREATE UNIQUE INDEX IF NOT EXISTS "
            + INDEX_ID + " ON " + TABLE_NAME + " ( " + _ID + " ) " + " ;";

    public static final String SQL_DROP_INDEX_ID = "DROP INDEX IF EXISTS "
            + INDEX_ID + " ;";

    public static final String[] DUMMIES_NAME = {
            "room1", "room2", "room3", "room4", "room5"
    };

    public static final String[] DUMMIES_MAP = {
            "room1.png", "room2.png", "room3.png", "room4.png", "room5.png"
    };

    public static final double[] DUMMIES_LONGITUDE = {
            10.19544666, 10.19544418, 10.19605976, 10.19560026, 10.19551413
    };

    public static final double[] DUMMIES_LATITUDE = {
            36.8378091, 36.83809698, 36.83864308, 36.83805462, 36.83887902
    };

    public static final double[] DUMMIES_ALTITUDE = {
            0, 0, 0, 0, 0
    };

    public static final String getInsert(int i) {
        String sql = "INSERT INTO "
                + TABLE_NAME
                + String.format(
                        "(%s, %s, %s, %s, %s)"//
                        , KEY_NAME, KEY_MAP, KEY_LONGITUDE, KEY_LATITUDE, KEY_ALTITUDE)
                + String.format(
                        " VALUES ('%s', '%s', %f, %f, %f);"//
                        , DUMMIES_NAME[i]
                        , DUMMIES_MAP[i]
                        , DUMMIES_LONGITUDE[i]
                        , DUMMIES_LATITUDE[i]
                        , DUMMIES_ALTITUDE[i]);
        return sql;
    }
}
