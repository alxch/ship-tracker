package com.mongodb.shiptracker.repository;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.shiptracker.config.MongodbConfig;
import com.mongodb.shiptracker.model.Location;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


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

  @Override
  public List<Document> calculateTotalDistanceTraveled() {
    MongoCollection<Document> collection = database.getCollection("shipLocations");

    // Step 1: Set window fields to shift coordinates for calculating the previous position
    Document setWindowFieldsStage = new Document("$setWindowFields",
      new Document("partitionBy", "$boatId")
        .append("sortBy", new Document("timestamp", 1L))
        .append("output",
          new Document("previousCoordinates",
            new Document("$shift",
              new Document("output", "$location.coordinates")
                .append("by", -1L)
            )
          )
        )
    );

    // Step 2: Calculate the distance between current and previous coordinates
    Document setDistanceStage = new Document("$set",
      new Document("distance",
        new Document("$sqrt",
          new Document("$add", Arrays.asList(
            new Document("$pow", Arrays.asList(
              new Document("$subtract", Arrays.asList(
                new Document("$arrayElemAt", Arrays.asList("$location.coordinates", 1L)),
                new Document("$arrayElemAt", Arrays.asList("$previousCoordinates", 1L))
              )),
              2L
            )),
            new Document("$pow", Arrays.asList(
              new Document("$subtract", Arrays.asList(
                new Document("$arrayElemAt", Arrays.asList("$location.coordinates", 0L)),
                new Document("$arrayElemAt", Arrays.asList("$previousCoordinates", 0L))
              )),
              2L
            ))
          ))
        )
      )
    );

    // Step 3: Group by boatId and calculate the total distance
    Document groupTotalDistanceStage = new Document("$group",
      new Document("_id", "$boatId")
        .append("totalDistance", new Document("$sum", "$distance"))
    );

    // Perform the aggregation and collect results into a list
    AggregateIterable<Document> result = collection.aggregate(Arrays.asList(setWindowFieldsStage, setDistanceStage, groupTotalDistanceStage));
    List<Document> resultList = new ArrayList<>();
    result.forEach(resultList::add);

    return resultList;
  }

}
