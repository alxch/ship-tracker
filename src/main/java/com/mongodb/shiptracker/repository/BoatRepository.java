package com.mongodb.shiptracker.repository;

import com.mongodb.shiptracker.model.Boat;

public interface BoatRepository {
    void addBoat(Boat boat);
}
