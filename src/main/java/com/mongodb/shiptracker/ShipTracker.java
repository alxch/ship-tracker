package com.mongodb.shiptracker;

import com.mongodb.shiptracker.model.Boat;
import com.mongodb.shiptracker.model.Location;
import com.mongodb.shiptracker.model.Port;
import com.mongodb.shiptracker.repository.BoatRepositoryImpl;
import com.mongodb.shiptracker.repository.PortRepositoryImpl;
import com.mongodb.shiptracker.service.Simulator;

import java.util.Arrays;
import java.util.List;

public class ShipTracker {
    public static void main(String[] args) {
        PortRepositoryImpl portRepository = new PortRepositoryImpl();
        BoatRepositoryImpl boatRepository = new BoatRepositoryImpl();
        Simulator simulator = new Simulator();

        // Insert initial ports into the database
        insertInitialPorts(portRepository);

        // Create boats using port names
        Boat boat1 = createBoat("BOAT001", "New York", "Rotterdam", portRepository, boatRepository);
        Boat boat2 = createBoat("BOAT002", "Savannah", "Antwerp", portRepository, boatRepository);
        Boat boat3 = createBoat("BOAT003", "Miami", "Lisbon", portRepository, boatRepository);
        Boat boat4 = createBoat("BOAT004", "Halifax", "Le Havre", portRepository, boatRepository);
        Boat boat5 = createBoat("BOAT005", "Charleston", "Hamburg", portRepository, boatRepository);

        // Add boats to the simulator
        List<Boat> boats = Arrays.asList(boat1, boat2, boat3, boat4, boat5);
        boats.forEach(simulator::addBoat);

        // Run the simulation
        simulator.runSimulation();
    }

    private static void insertInitialPorts(PortRepositoryImpl repository) {
        List<Port> ports = Arrays.asList(
                new Port("New York", 40.7128, -74.0060),
                new Port("Rotterdam", 51.9244, 4.4777),
                new Port("Savannah", 32.0835, -81.0998),
                new Port("Antwerp", 51.2194, 4.4025),
                new Port("Miami", 25.7617, -80.1918),
                new Port("Lisbon", 38.7223, -9.1393),
                new Port("Halifax", 44.6488, -63.5752),
                new Port("Le Havre", 49.4944, 0.1079),
                new Port("Charleston", 32.7765, -79.9311),
                new Port("Hamburg", 53.5511, 9.9937)
        );

        ports.forEach(repository::addPort);
    }

    private static Boat createBoat(String boatId, String startPortName, String endPortName, PortRepositoryImpl portRepository, BoatRepositoryImpl boatRepository) {
        Port startPort = portRepository.getPortByName(startPortName);
        Port endPort = portRepository.getPortByName(endPortName);

        if (startPort != null && endPort != null) {
            Boat boat = new Boat(
                    boatId,
                    new Location(startPort.getLatitude(), startPort.getLongitude()),
                    new Location(endPort.getLatitude(), endPort.getLongitude())
            );
            boatRepository.addBoat(boat);
            return boat;
        } else {
            throw new IllegalArgumentException("One or both ports not found in the database");
        }
    }
}
