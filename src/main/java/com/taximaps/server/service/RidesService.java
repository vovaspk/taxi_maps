package com.taximaps.server.service;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.RideEntity;
import com.taximaps.server.entity.dto.FullRideDto;
import com.taximaps.server.entity.dto.RideFormDto;
import com.taximaps.server.entity.status.RideStatus;

import java.io.IOException;
import java.util.List;

public interface RidesService {
    List<RideEntity> findAll();
    List<RideEntity> findRidesByUserId(Long id);
    RideEntity findRideById(Long id);
    FullRideDto saveRide(RideFormDto rideFormDto, String userName) throws InterruptedException, ApiException, IOException;
    void updateRideStatus(RideStatus status, Long rideId);
    void updateRideRating(Long rideId, int rating);
    double calculatePrice(String origin, String dest, CarType carType) throws InterruptedException, ApiException, IOException;
    String calculateTimeOfRide(String origin, String dest, CarType carType) throws InterruptedException, ApiException, IOException;
    String calculateTimeFromDriverToPassenger(String passengerLocation, String driverLocation, CarType carType) throws InterruptedException, ApiException, IOException;
}
