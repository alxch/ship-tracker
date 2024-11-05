package com.mongodb.shiptracker.repository;

import com.mongodb.shiptracker.model.Port;

public interface PortRepository {
    void addPort(Port port);
    Port getPortByName(String name);
}
