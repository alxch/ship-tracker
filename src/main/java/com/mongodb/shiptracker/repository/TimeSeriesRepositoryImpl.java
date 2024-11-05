package com.mongodb.shiptracker.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.shiptracker.config.MongodbConfig;
import com.mongodb.shiptracker.model.Location;
import org.bson.Document;

import java.util.Arrays;
import java.util.Date;

public class TimeSeriesRepositoryImpl implements TimeSeriesRepository {

    private final MongoDatabase database;

    public TimeSeriesRepositoryImpl() {
        this.database = MongodbConfig.getDatabase();
    }

    @Override
    public void logBoatLocation(String boatId, Location location) {
        MongoCollection<Document> collection = database.getCollection("shipLocations");
        Document geoJsonLocation = new Document("type", "Point")
                .append("coordinates", Arrays.asList(location.getLongitude(), location.getLatitude()));

        Document logEntry = new Document("boatId", boatId)
                .append("timestamp", new Date())
                .append("location", geoJsonLocation);

        collection.insertOne(logEntry);
    }
}
