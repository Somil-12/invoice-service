package com.example.invoice.repo;

import com.example.invoice.model.Vehicle;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class VehicleRepository {
    private final Map<String, Vehicle> store = new ConcurrentHashMap<>();

    public Vehicle findById(String vehicleId) {
        return store.get(vehicleId);
    }

    public void save(Vehicle vehicle) {
        store.put(vehicle.getVehicleId(), vehicle);
    }
}
