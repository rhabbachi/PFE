
package com.tunav.tunavmedi.datatype;

import android.os.SystemClock;
import android.text.Html;
import android.text.Spanned;

public class Patient {

    private Long mID = null;
    private String mName = null;
    private Long mInTime = null;
    private String mRecord = null;
    private Boolean urgent = false;
    private Boolean mAlarmeOn = false;
    private Long mAlarme = null;
    private Placemark mPlacemark = null;
    private String mPhoto = null;
    private Long mLastUpdate = null;

    public Patient(Long id, String source, Long interned,
            String description) {
        mID = id;
        mName = source;
        mInTime = interned;
        mRecord = description;
        mLastUpdate = SystemClock.currentThreadTimeMillis();
    }

    public Boolean alarmeOn() {
        return mAlarmeOn;
    }

    public Long getAlarme() {
        return mAlarme;
    }

    public long getId() {
        return mID;
    }

    public Long getInTime() {
        return mInTime;
    }

    public Long getLastUpdate() {
        return mLastUpdate;
    }

    public String getName() {
        return mName;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public Placemark getPlacemark() {
        return mPlacemark;
    }

    public String getRecord() {
        return mRecord;
    }

    public Spanned getRecordHTML() {
        return Html.fromHtml(mRecord);
    }

    public Boolean isUrgent() {
        return urgent;
    }

    public void setAlarme(Long alarme) {
        mAlarme = alarme;
        setLastUpdateNow();

    }

    public void setAlarmeOn(boolean on) {
        this.mAlarmeOn = on;
        setLastUpdateNow();
    }

    public void setLastUpdate(Long lastUpdate) {
        this.mLastUpdate = lastUpdate;
    }

    public void setLastUpdateNow() {
        mLastUpdate = SystemClock.currentThreadTimeMillis();
    }

    public void setPhoto(String photoPath) {
        this.mPhoto = photoPath;
    }

    public void setPlacemark(Placemark placemark) {
        this.mPlacemark = placemark;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
        setLastUpdateNow();
    }

    @Override
    public String toString() {
        // TODO
        return null;
    }
}
