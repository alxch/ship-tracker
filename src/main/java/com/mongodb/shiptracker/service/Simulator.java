package com.mongodb.shiptracker.service;

import com.mongodb.shiptracker.model.Boat;
import com.mongodb.shiptracker.repository.TimeSeriesRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class Simulator {
    private final List<Boat> boats;
    private final TimeSeriesRepositoryImpl timeSeriesRepositoryImpl;

    public Simulator() {
        this.boats = new ArrayList<>();
        this.timeSeriesRepositoryImpl = new TimeSeriesRepositoryImpl(); // Initialize the time series repository
    }

    public void addBoat(Boat boat) {
        boats.add(boat);
    }

    // Run the simulation until each boat reaches its destination
    public void runSimulation() {
        boolean hasActiveBoats = true;

        while (hasActiveBoats) {
            System.out.println("Simulation step");
            hasActiveBoats = false;

            // Move all boats in one step and log their locations
            for (Boat boat : new ArrayList<>(boats)) {
                if (!(boat.getLocation().getLatitude() == boat.getDestination().getLatitude() &&
                        boat.getLocation().getLongitude() == boat.getDestination().getLongitude())) {
                    boat.move();
                    timeSeriesRepositoryImpl.logBoatLocation(boat.getBoatId(), boat.getLocation()); // Log the new location

                    System.out.println("Boat " + boat.getBoatId() + " moved to new position: (" +
                            boat.getLocation().getLatitude() + ", " + boat.getLocation().getLongitude() + ")");

                    hasActiveBoats = true;
                } else {
                    System.out.println("Boat " + boat.getBoatId() + " has reached its destination.");
                }
            }

            // Remove boats that have reached their destinations after all have been processed
            boats.removeIf(boat -> boat.getLocation().getLatitude() == boat.getDestination().getLatitude() &&
                    boat.getLocation().getLongitude() == boat.getDestination().getLongitude());

            try {
                Thread.sleep(100); // Pause for 1 second between steps to simulate real-time updates
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("All boats have reached their destinations. Simulation complete.");
    }
}
