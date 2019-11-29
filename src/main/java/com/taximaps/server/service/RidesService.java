package com.taximaps.server.service;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.Ride;
import com.taximaps.server.entity.RideType;
import com.taximaps.server.entity.status.RideStatus;

import java.io.IOException;
import java.util.List;

public interface RidesService {
    List<Ride> findAll();
    List<Ride> findRidesByUserId(Long id);
    Ride findRideById(Long id);
    boolean save(Ride ride);
    void updateRideStatus(RideStatus status, Long rideId);
    double calculatePrice(String origin, String dest, RideType rideType) throws InterruptedException, ApiException, IOException;
    String calculateTime(String origin, String dest, RideType rideType) throws InterruptedException, ApiException, IOException;
}
