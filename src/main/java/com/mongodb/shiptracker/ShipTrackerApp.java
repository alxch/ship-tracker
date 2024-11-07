package com.mongodb.shiptracker;

import com.mongodb.shiptracker.controller.ShipTrackerController;
import io.javalin.Javalin;

public class ShipTrackerApp {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);

        ShipTrackerController controller = new ShipTrackerController();
        controller.registerRoutes(app);
    }
}
