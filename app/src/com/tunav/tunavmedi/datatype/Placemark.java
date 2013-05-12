
package com.tunav.tunavmedi.datatype;

import android.location.Location;

public class Placemark {
    private String name;
    private String description;
    private Location location;

    public Placemark(String name, String description, Location point) {
        this.name = name;
        this.description = description;
        this.location = point;
    }

    public String getDescription() {
        return description;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(Location point) {
        this.location = point;
    }

    public void setName(String name) {
        this.name = name;
    }
}
