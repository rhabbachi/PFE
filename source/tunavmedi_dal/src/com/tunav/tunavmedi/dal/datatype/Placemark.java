
package com.tunav.tunavmedi.dal.datatype;

import android.location.Location;

public class Placemark {
    private String name;
    private String map;
    private Location location;

    public Placemark(String name, String path, Location point) {
        this.name = name;
        this.map = path;
        this.location = point;
    }

    public String getMapPath() {
        return map;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public void setMapPath(String description) {
        this.map = description;
    }

    public void setLocation(Location point) {
        this.location = point;
    }

    public void setName(String name) {
        this.name = name;
    }
}
