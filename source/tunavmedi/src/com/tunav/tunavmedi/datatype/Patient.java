
package com.tunav.tunavmedi.datatype;

import android.text.Html;
import android.text.Spanned;
import android.text.format.Time;

public class Patient {

    private Long mID = null;
    private String mName = null;
    private Long mInterned = null;
    private String mRecord = null;
    private Boolean mUrgent = false;
    private Placemark mPlacemark = null;
    private String mPhoto = null;
    private Long mUpdated = null;

    public Patient(Long id, String source, Long interned,
            String description) {
        mID = id;
        mName = source;
        mInterned = interned;
        mRecord = description;
        setUpdatedNow();
    }

    public Patient(Long id, String source, Long interned,
            String description, Long updated) {
        mID = id;
        mName = source;
        mInterned = interned;
        mRecord = description;
        mUpdated = updated;
    }

    public long getId() {
        return mID;
    }

    public Long getInterned() {
        return mInterned;
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

    public Long getUpdated() {
        return mUpdated;
    }

    public Boolean isUrgent() {
        return mUrgent;
    }

    public void setPhoto(String photoPath) {
        this.mPhoto = photoPath;
    }

    public void setPlacemark(Placemark placemark) {
        this.mPlacemark = placemark;
    }

    public void setUpdated(Long lastUpdate) {
        this.mUpdated = lastUpdate;
    }

    public void setUpdatedNow() {
        Time now = new Time();
        now.setToNow();
        mUpdated = now.toMillis(true);
    }

    public void setUrgent(boolean urgent) {
        this.mUrgent = urgent;
        setUpdatedNow();
    }

    @Override
    public String toString() {
        // TODO
        return null;
    }
}
