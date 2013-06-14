
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

    public static final double[][] DUMMIES_LOCATIONS_HOME = {
            {
                    36.83911707, 10.19574105, 0
            },
            {
                    36.83919173, 10.1957513, 0
            },
            {
                    36.83807939, 10.19529068, 0
            },
            {
                    36.83892536, 10.19512774, 0
            },
            {
                    36.8385787, 10.19697297, 0
            }
    };

    public static final double[][] DUMMIES_LOCATIONS_TUNAV = {
            {
                    36.89558244, 10.18611391, 0
            },
            {
                    36.89518587, 10.18502959, 0
            },
            {
                    36.89560655, 10.1866753, 0
            },
            {
                    36.89564166, 10.18693307, 0
            },
            {
                    36.89502938, 10.18655187, 0
            }
    };

    public static double[][] getDummyLocations() {
        return DUMMIES_LOCATIONS_TUNAV;
    }

    public static final String getInsert(int i) {
        String sql = "INSERT INTO "
                + TABLE_NAME
                + String.format(
                        "(%s, %s, %s, %s, %s)"//
                        , KEY_NAME, KEY_MAP, KEY_LATITUDE, KEY_LONGITUDE, KEY_ALTITUDE)
                + String.format(
                        " VALUES ('%s', '%s', %f, %f, %f);"//
                        , DUMMIES_NAME[i]
                        , DUMMIES_MAP[i]
                        , getDummyLocations()[i][0]
                        , getDummyLocations()[i][1]
                        , getDummyLocations()[i][2]);
        return sql;
    }
}
