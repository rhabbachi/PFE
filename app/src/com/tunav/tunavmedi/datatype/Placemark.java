
package com.tunav.tunavmedi.datatype;

import android.location.Location;

public class Placemark implements Comparable<Placemark> {
    private String name;
    private String description;
    private Location location;

    public Placemark(String name, String description, Location point) {
        this.name = name;
        this.description = description;
        this.location = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location point) {
        this.location = point;
    }

    @Override
    public int compareTo(Placemark another) {
        float distance = location.distanceTo(another.getLocation());
        return Math.round(distance);
    }
}
