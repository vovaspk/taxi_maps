package com.taximaps.server.service;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.Ride;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.RideFormDto;
import com.taximaps.server.entity.status.RideStatus;

import java.io.IOException;
import java.util.List;

public interface RidesService {
    List<Ride> findAll();
    List<Ride> findRidesByUserId(Long id);
    Ride findRideById(Long id);
    boolean save(Ride ride);
    void createRide(RideFormDto rideFormDto, User user, Car car) throws InterruptedException, ApiException, IOException;
    void updateRideStatus(RideStatus status, Long rideId);
    double calculatePrice(String origin, String dest, CarType carType) throws InterruptedException, ApiException, IOException;
    String calculateTimeOfRide(String origin, String dest, CarType carType) throws InterruptedException, ApiException, IOException;
    String calculateTimeFromDriverToPassanger(String passangerLocation, String driverLocation, CarType carType) throws InterruptedException, ApiException, IOException;
}
