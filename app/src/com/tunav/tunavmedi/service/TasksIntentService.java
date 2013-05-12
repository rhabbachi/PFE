
package com.tunav.tunavmedi.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Binder;
import android.util.Log;
import android.widget.Toast;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.activity.MainActivity;

public class TasksIntentService extends IntentService {

    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {

        public TasksIntentService getService() {
            // Return this instance of LocalService so clients can call public
            // methods
            Log.v(tag, "getService()");
            return TasksIntentService.this;
        }
    }

    private enum LocationCriteria {
        POWER_HIGH(Criteria.ACCURACY_FINE, Criteria.POWER_HIGH, RADIUS_HIGH, DELAY_HIGH), //
        POWER_LOW(Criteria.ACCURACY_COARSE, Criteria.POWER_LOW, RADIUS_LOW, DELAY_LOW);//

        Integer mCriterionAccuracy = null;
        Integer mCriterionPowerRequirement = null;
        Integer mRadius = null;
        Integer mDelay = null;

        LocationCriteria(int criterionAccuracy, int criterionPowerRequirement, int radius, int delay) {
            mCriterionAccuracy = criterionAccuracy;
            mCriterionPowerRequirement = criterionPowerRequirement;
            mRadius = radius;
            mDelay = delay;
        }

        public Integer getCriterionAccuracy() {
            return mCriterionAccuracy;
        }

        public Integer getCriterionPowerRequirement() {
            return mCriterionPowerRequirement;
        }

        public Integer getRadius() {
            return mRadius;
        }
    }

    // TODO check constants validity
    static final Integer RADIUS_HIGH = 3;
    static final Integer RADIUS_LOW = 9;
    static final Integer DELAY_HIGH = 1000 * 60 * 10;
    static final Integer DELAY_LOW = 1000 * 60 * 20;

    private LocationCriteria currentCriteria = null;

    private NotificationManager mNotificationManager = null;

    public static final String tag = "TasksIntentService";

    private Runnable locationWorker = new Runnable() {

        private Criteria generateCriteria() {

            Criteria criteria = new Criteria();
            criteria.setAccuracy(currentCriteria.getCriterionAccuracy());
            criteria.setPowerRequirement(currentCriteria.getCriterionPowerRequirement());
            criteria.setAltitudeRequired(true);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);

            return criteria;
        }

        @Override
        public void run() {
            if (currentCriteria != null) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                String bestProvider = locationManager.getBestProvider(generateCriteria(),
                        false);
                // TODO
            }
        }
    };

    public TasksIntentService() {
        super(tag);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        showNotification();
    }

    @Override
    public void onDestroy() {
        mNotificationManager.cancel(R.id.tasks_service_notification);
        // Tell the user we stopped.
        Toast.makeText(this, R.string.tasks_service_stopped, Toast.LENGTH_SHORT).show();
        // TODO stop threads and clean up
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(tag, "onHandleIntent()");
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the
        // expanded notification
        CharSequence text = getText(R.string.tasks_service_started);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.hospital, text,
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this
        // notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.tasks_service_label),
                text, contentIntent);

        // Send the notification.
        mNotificationManager.notify(R.id.tasks_service_notification, notification);
    }
}
