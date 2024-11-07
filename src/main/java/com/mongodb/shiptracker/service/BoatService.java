package com.mongodb.shiptracker.service;

import com.mongodb.shiptracker.model.Boat;
import com.mongodb.shiptracker.model.Location;
import com.mongodb.shiptracker.model.Port;
import com.mongodb.shiptracker.repository.BoatRepositoryImpl;
import com.mongodb.shiptracker.repository.PortRepositoryImpl;

public class BoatService {
    private final PortRepositoryImpl portRepository;
    private final BoatRepositoryImpl boatRepository;

    public BoatService() {
        this.portRepository = new PortRepositoryImpl();
        this.boatRepository = new BoatRepositoryImpl();
    }

    public Boat createBoat(String boatId, String startPortName, String endPortName) {
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
            return null;
        }
    }
}
