package com.mongodb.shiptracker.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.shiptracker.config.MongodbConfig;
import com.mongodb.shiptracker.model.Boat;
import com.mongodb.shiptracker.model.Location;
import org.bson.Document;


public class BoatRepositoryImpl implements BoatRepository {
    private final MongoDatabase database;

    public BoatRepositoryImpl() {
        this.database = MongodbConfig.getDatabase();
    }

    @Override
    public void addBoat(Boat boat) {
        MongoCollection<Document> collection = database.getCollection("boats");
        collection.insertOne(boat.toDocument());
    }

    private Boat documentToBoat(Document document) {
        return new Boat(
                document.getObjectId("_id"),
                document.getString("boatId"),
                new Location(
                        document.get("location", Document.class).getDouble("latitude"),
                        document.get("location", Document.class).getDouble("longitude")
                ),
                new Location(
                        document.get("destination", Document.class).getDouble("latitude"),
                        document.get("destination", Document.class).getDouble("longitude")
                )
        );
    }
}
