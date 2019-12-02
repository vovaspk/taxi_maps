package com.taximaps.server.service.impl;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.RideType;
import com.taximaps.server.entity.status.CarStatus;
import com.taximaps.server.entity.status.RideStatus;
import com.taximaps.server.maps.JsonReader;
import com.taximaps.server.repository.CarRepository;
import com.taximaps.server.repository.RidesRepository;
import com.taximaps.server.entity.Ride;
import com.taximaps.server.service.CarService;
import com.taximaps.server.service.RidesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class RidesServiceImpl implements RidesService {

    private RidesRepository ridesRepository;
    private CarService carService;
    private static final double timeFor1KM = 2.5;
    private static final double priceFor1KM = 11;

    public List<Ride> findAll() {
        return ridesRepository.findAll();
    }

    public List<Ride> findRidesByUserId(Long id) {
        return ridesRepository.findRidesByUserId(id);
    }

    @Override
    public Ride findRideById(Long id) {
        return ridesRepository.findRideById(id);
    }

    @Override
    public boolean save(Ride ride) {
        ridesRepository.save(ride);
//        new java.util.Timer().schedule(
//                new java.util.TimerTask() {
//                    @Override
//                    public void run() {
//
//                      carService.setCarFree(ride.getCar());
//                    }
//                },
//                5000
//        );
        //TODO check if carService setCar free changes car to free in ride table with foreign key
        return true;
    }

    @Override
    public void updateRideStatus(RideStatus status, Long rideId) {
        Ride ride = ridesRepository.findRideById(rideId);
        ride.setStatus(status);
        ridesRepository.save(ride);
    }

    @Override
    public double calculatePrice(String origin, String dest, RideType rideType) throws InterruptedException, ApiException, IOException {
        double distance = JsonReader.getDriveDistance(origin, dest);

        double price = (distance/1000) * priceFor1KM;
        return price;
    }

    @Override
    public String calculateTime(String origin, String dest, RideType rideType) throws InterruptedException, ApiException, IOException {
        String time = JsonReader.getDriveTime(origin, dest);
        return time;
    }


}
