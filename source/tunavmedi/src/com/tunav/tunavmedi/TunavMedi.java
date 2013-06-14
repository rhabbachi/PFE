
package com.tunav.tunavmedi;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.util.Log;

public class TunavMedi extends Application {

    private static String tag = "Tunavmedi";
    private static Context context;
    // DEV MODE
    public static final boolean DEVELOPER_MODE = true;

    // LOCATION center, min, max
    public static final int LOCATION_ALTITUDE = 2;
    public static final int LOCATION_LATITUDE = 0;
    public static final int LOCATION_LONGITUDE = 1;

    public static final int LOCATION_NOMINAL = 0;
    public static final int LOCATION_MIN = 1;
    public static final int LOCATION_MAX = 2;

    public static final double[][] LOCATION_TUNAV = {
            {
                    36.895238, 10.185993, 10
            },
            {
                    36.895000, 10.185000, 0
            },
            {
                    36.896000, 10.186000, 20
            }
    };

    public static final double[][] LOCATION_HOME = {
            {
                    36.838433, 10.196009, 10
            },
            {
                    36.838000, 10.196000, 0
            },
            {
                    36.839000, 10.197000, 20
            }
    };

    public static final double[] LOCATION_ENIG = {

            };

    public static Location devFakeLocation = null;

    public static float OUT_OF_REACH = 1000;

    public static boolean debugLocation() {
        if (DEVELOPER_MODE) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean debugNotification() {
        if (DEVELOPER_MODE) {
            return true;
        } else {
            return false;
        }
    }

    public static Context getContext() {
        return context;
    }

    public static double[][] getDebugLocation() {
        return LOCATION_TUNAV;
    }

    @Override
    public void onCreate() {
        Log.d(tag, this.toString());
        super.onCreate();
        context = this;
    }
}
