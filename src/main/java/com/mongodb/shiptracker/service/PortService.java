package com.mongodb.shiptracker.service;

import com.mongodb.shiptracker.model.Port;
import com.mongodb.shiptracker.repository.PortRepositoryImpl;

import java.util.Arrays;
import java.util.List;

public class PortService {
    private final PortRepositoryImpl portRepository;

    public PortService() {
        this.portRepository = new PortRepositoryImpl();
    }

    public void insertInitialPorts() {
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
        ports.forEach(portRepository::addPort);
    }
}
