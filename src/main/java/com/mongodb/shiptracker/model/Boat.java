package com.mongodb.shiptracker.model;

import org.bson.Document;
import org.bson.types.ObjectId;

public class Boat {
    private ObjectId _id;
    private final String boatId;
    private Location location;
    private Location destination;

    public Boat(ObjectId _id, String boatId, Location location, Location destination) {
        this._id = _id;
        this.boatId = boatId;
        this.location = location;
        this.destination = destination;
    }

    public Boat(String boatId, Location location, Location destination) {
        this._id = new ObjectId();
        this.boatId = boatId;
        this.location = location;
        this.destination = destination;
    }

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public String getBoatId() {
        return boatId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public Document toDocument() {
        return new Document("_id", _id)
                .append("boatId", boatId)
                .append("startLocation", new Document("latitude", location.getLatitude())
                        .append("longitude", location.getLongitude()))
                .append("destination", new Document("latitude", destination.getLatitude())
                        .append("longitude", destination.getLongitude()));
    }

    // Simulate movement by moving the boat incrementally towards its destination
    public void move() {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        double destLatitude = destination.getLatitude();
        double destLongitude = destination.getLongitude();

        double nauticalMilesPerHour = 20;
        double stepSizeInDegrees = nauticalMilesPerHour / 60.0; // Convert nautical miles to degrees

        // Calculate direction
        double latDirection = destLatitude - currentLatitude;
        double lonDirection = destLongitude - currentLongitude;
        double distance = Math.sqrt(latDirection * latDirection + lonDirection * lonDirection);

        if (distance > stepSizeInDegrees) {
            // Normalize direction
            latDirection /= distance;
            lonDirection /= distance;

            // Update current location based on step size
            double newLatitude = currentLatitude + latDirection * stepSizeInDegrees;
            double newLongitude = currentLongitude + lonDirection * stepSizeInDegrees;

            location.setLatitude(newLatitude);
            location.setLongitude(newLongitude);
        } else {
            // If the boat is close enough, set it directly to the destination
            location.setLatitude(destLatitude);
            location.setLongitude(destLongitude);
        }
    }
}
