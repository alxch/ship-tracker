package com.mongodb.shiptracker.repository;

import com.mongodb.shiptracker.model.Location;
import org.bson.Document;

import java.util.List;

public interface TimeSeriesRepository {
    void logBoatLocation(String boatId, Location location);
    List<Document> calculateTotalDistanceTraveled();
}
