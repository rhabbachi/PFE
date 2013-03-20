package com.tunav.tunavmedi.datatypes;

import android.location.Location;

public class Placemark {
    private String name;
    private String description;
    private Location point;

    public Placemark(String name, String description, Location point) {
	this.name = name;
	this.description = description;
	this.point = point;
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

    public Location getPoint() {
	return point;
    }

    public void setPoint(Location point) {
	this.point = point;
    }
}
