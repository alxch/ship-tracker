package com.mongodb.shiptracker.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.TimeSeriesGranularity;
import com.mongodb.client.model.TimeSeriesOptions;

public class MongodbConfig {
  private static final String CONNECTION_STRING = "mongodb://127.0.0.1:27017/";
  private static final String DB_NAME = "ShipTracker";
  private static final String COLLECTION_NAME = "shipLocations";
  private static final String TIME_FIELD = "timestamp";
  private static final String META_FIELD = "boatId";
  private static final TimeSeriesGranularity GRANULARITY = TimeSeriesGranularity.SECONDS;
  
  private static MongoDatabase database;

  public static MongoDatabase getDatabase() {
    if (database == null) {
      database = initializeDatabase();
    }
    createTimeSeriesCollectionIfNotExists(database, COLLECTION_NAME);

    return database;
  }

  private static MongoDatabase initializeDatabase() {
    ServerApi serverApi = ServerApi.builder()
      .version(ServerApiVersion.V1)
      .build();
    MongoClientSettings settings = MongoClientSettings.builder()
      .applyConnectionString(new ConnectionString(CONNECTION_STRING))
      .serverApi(serverApi)
      .build();

    MongoClient mongoClient = MongoClients.create(settings);
    MongoDatabase db = mongoClient.getDatabase(DB_NAME);

    return db;
  }

  private static void createTimeSeriesCollectionIfNotExists(MongoDatabase db, String collectionName) {

    MongoIterable<String> collections = db.listCollectionNames();
    for (String name : collections) {
      if (name.equals(collectionName)) {
        System.out.println("Time series collection '" + collectionName + "' already exists.");
        return;
      }
    }

    TimeSeriesOptions timeSeriesOptions = new TimeSeriesOptions(TIME_FIELD)
      .metaField(META_FIELD)
      .granularity(GRANULARITY);

    CreateCollectionOptions collOptions = new CreateCollectionOptions().timeSeriesOptions(timeSeriesOptions);

    db.createCollection(collectionName, collOptions);
    System.out.println("Time series collection '" + collectionName + "' created.");
  }
}