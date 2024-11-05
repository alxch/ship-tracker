package com.mongodb.shiptracker.repository;

import com.mongodb.shiptracker.model.Location;

public interface TimeSeriesRepository {
    void logBoatLocation(String boatId, Location location);
}
