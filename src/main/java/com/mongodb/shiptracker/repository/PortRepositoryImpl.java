package com.mongodb.shiptracker.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.shiptracker.config.MongodbConfig;
import com.mongodb.shiptracker.model.Port;
import org.bson.Document;

import java.util.List;

public class PortRepositoryImpl implements PortRepository {
    private final MongoDatabase database;

    public PortRepositoryImpl() {
        this.database = MongodbConfig.getDatabase();
    }

    @Override
    public void addPort(Port port) {
        MongoCollection<Document> collection = database.getCollection("ports");
        collection.insertOne(port.toDocument());
    }

    @Override
    public Port getPortByName(String name) {
        MongoCollection<Document> collection = database.getCollection("ports");
        Document query = new Document("name", name);
        Document result = collection.find(query).first();

        if (result != null) {
            List<Double> coordinates = result.get("location", Document.class).getList("coordinates", Double.class);
            return new Port(
                    result.getString("name"),
                    coordinates.get(1), // Latitude
                    coordinates.get(0)  // Longitude
            );
        }
        return null;
    }
}
