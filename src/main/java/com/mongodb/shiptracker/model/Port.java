package com.mongodb.shiptracker.model;

import org.bson.Document;

import java.util.Arrays;

public class Port {
    private String name;
    private double latitude;
    private double longitude;

    public Port(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Document toDocument() {
        return new Document("name", name)
                .append("location", new Document("type", "Point")
                        .append("coordinates", Arrays.asList(longitude, latitude))); // GeoJSON format: [longitude, latitude]
    }
}
