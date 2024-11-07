package com.mongodb.shiptracker.service;

import com.mongodb.shiptracker.repository.TimeSeriesRepositoryImpl;
import org.bson.Document;

import java.util.List;

public class DistanceService {
    private final TimeSeriesRepositoryImpl timeSeriesRepository;

    public DistanceService() {
        this.timeSeriesRepository = new TimeSeriesRepositoryImpl();
    }

    public List<Document> calculateTotalDistanceTraveled() {
        return timeSeriesRepository.calculateTotalDistanceTraveled();
    }
}
