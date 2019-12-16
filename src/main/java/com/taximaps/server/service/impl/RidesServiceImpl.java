package com.taximaps.server.service.impl;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.RideFormDto;
import com.taximaps.server.entity.status.RideStatus;
import com.taximaps.server.mapper.LocationMapper;
import com.taximaps.server.maps.JsonReader;
import com.taximaps.server.repository.LocationRepository;
import com.taximaps.server.repository.RidesRepository;
import com.taximaps.server.entity.Ride;
import com.taximaps.server.service.CarService;
import com.taximaps.server.service.RidesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class RidesServiceImpl implements RidesService {

    private RidesRepository ridesRepository;
    private LocationRepository locationRepository;
    private CarService carService;
    private LocationMapper locationMapper;
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
        carService.setCarOnWay(ride.getCar());
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {

                      carService.setCarFree(ride.getCar());
                      carService.changeCarLocation(ride.getCar(), ride.getDestination());

                    }
                },
                12000
        //ride.getRideTime().getTime()
        );
        long tm = ride.getRideTime().getTime();
        return true;
    }

    @Override
    public void createRide(RideFormDto rideFormDto, User user, Car foundCar) throws InterruptedException, ApiException, IOException {
        this.save(Ride.builder()
                .rideTime(Time.valueOf(LocalTime.now()))
                .rideDate(rideFormDto.getDate())
                .startPoint(locationMapper.fromCoordsToLocation(rideFormDto.getOrigin()))
                .car(foundCar)
                .user(user)
                .status(RideStatus.NEW_RIDE)
                //.price()
                .build()
        );

    }

    @Override
    public void updateRideStatus(RideStatus status, Long rideId) {
        Ride ride = ridesRepository.findRideById(rideId);
        ride.setStatus(status);
        ridesRepository.save(ride);
    }

    @Override
    public double calculatePrice(String origin, String dest, CarType carType) throws InterruptedException, ApiException, IOException {
        //TODO include carType in price, luxury must have higher cost than regular...
        double distance = JsonReader.getDriveDistance(origin, dest);
        return (distance/1000) * priceFor1KM;
    }

    @Override
    public String calculateTimeOfRide(String origin, String dest, CarType carType) throws InterruptedException, ApiException, IOException {
        return JsonReader.getDriveTime(origin, dest);
    }

    @Override
    public String calculateTimeFromDriverToPassanger(String passangerLocation, String driverLocation, CarType carType) throws InterruptedException, ApiException, IOException {
        return JsonReader.getDriveTime(driverLocation, passangerLocation);
    }


}
