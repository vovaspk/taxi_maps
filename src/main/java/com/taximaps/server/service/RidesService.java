package com.taximaps.server.service;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.RideEntity;
import com.taximaps.server.entity.dto.ride.FullRideDto;
import com.taximaps.server.entity.dto.ride.FullRideInfoDto;
import com.taximaps.server.entity.dto.ride.RideFormDto;
import com.taximaps.server.entity.status.RideStatus;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

public interface RidesService {
    List<RideEntity> findAll();
    List<RideEntity> findUserRides(String username);
    RideEntity findRideById(Long id);
    String getFirstRoute(RideFormDto rideFormDto);
    String getSecondRoute(RideFormDto rideFormDto);
    FullRideDto bookRide(RideFormDto rideFormDto, String userName) throws InterruptedException, ApiException, IOException;
    FullRideInfoDto getRideInfoBeforeRide(RideFormDto rideFormDto) throws InterruptedException, ApiException, IOException;
    void updateRideStatus(RideStatus status, Long rideId);
    void updateRideRating(Long rideId, int rating);
    double calculatePrice(double distance, CarType carType);
    double getDistance(String origin, String dest) throws InterruptedException, ApiException, IOException;
    String calculateTimeOfRide(String origin, String dest, CarType carType) throws InterruptedException, ApiException, IOException;
    String calculateTimeFromDriverToPassenger(String passengerLocation, String driverLocation, CarType carType) throws InterruptedException, ApiException, IOException;
}
