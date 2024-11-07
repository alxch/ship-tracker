package com.mongodb.shiptracker.controller;

import com.mongodb.shiptracker.model.Boat;
import com.mongodb.shiptracker.model.BoatRequest;
import com.mongodb.shiptracker.service.BoatService;
import com.mongodb.shiptracker.service.DistanceService;
import com.mongodb.shiptracker.service.PortService;
import com.mongodb.shiptracker.service.Simulator;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class ShipTrackerController {
    private final PortService portService;
    private final BoatService boatService;
    private final Simulator simulator;
    private final DistanceService distanceService;

    public ShipTrackerController() {
        this.portService = new PortService();
        this.boatService = new BoatService();
        this.simulator = new Simulator();
        this.distanceService = new DistanceService();
    }

    public void registerRoutes(Javalin app) {
        app.post("/ports/init", this::insertInitialPorts); // Initializes ports
        app.post("/boats", this::createBoat); // Creates a boat
        app.post("/simulate", this::runSimulation); // Runs the simulation
        app.get("/boats/totalDistance", this::getTotalDistances); // Gets total distance traveled for all boats

        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace(); // Logs the full stack trace for debugging
            ctx.status(500).result("Internal Server Error: " + e.getMessage());
        });
    }

    private void insertInitialPorts(Context ctx) {
        portService.insertInitialPorts();
        ctx.status(201).result("Ports initialized.");
    }

    private void createBoat(Context ctx) {
        System.out.println("Received JSON: " + ctx.body()); // Log raw JSON for verification

        BoatRequest boatRequest = ctx.bodyAsClass(BoatRequest.class);

        System.out.println(boatRequest.getStartPort());

        if (boatRequest.getBoatId() == null) {
            ctx.status(400).result("Missing 'boatId' parameter.");
            return;
        }
        if (boatRequest.getStartPort() == null) {
            ctx.status(400).result("Missing 'startPort' parameter.");
            return;
        }
        if (boatRequest.getEndPort() == null) {
            ctx.status(400).result("Missing 'endPort' parameter.");
            return;
        }

        Boat boat = boatService.createBoat(boatRequest.getBoatId(), boatRequest.getStartPort(), boatRequest.getEndPort());
        if (boat != null) {
            simulator.addBoat(boat);
            ctx.status(201).json(boat);
        } else {
            ctx.status(404).result("One or both ports not found.");
        }
    }

    private void runSimulation(Context ctx) {
        simulator.runSimulation();
        ctx.status(200).result("Simulation complete.");
    }

    private void getTotalDistances(Context ctx) {
        ctx.json(distanceService.calculateTotalDistanceTraveled());
    }
}
