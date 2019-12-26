package com.taximaps.server.service.impl;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.RideFormDto;
import com.taximaps.server.entity.status.RideStatus;
import com.taximaps.server.mapper.LocationMapper;
import com.taximaps.server.maps.JsonReader;
import com.taximaps.server.repository.LocationRepository;
import com.taximaps.server.repository.RidesRepository;
import com.taximaps.server.entity.RideEntity;
import com.taximaps.server.repository.UserRepository;
import com.taximaps.server.service.CarService;
import com.taximaps.server.service.RidesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;
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
    private UserRepository userRepository;
    private CarService carService;
    private LocationMapper locationMapper;
    private static final double timeFor1KM = 2.5;
    private static final double priceFor1KM = 11;

    public List<RideEntity> findAll() {
        return ridesRepository.findAll();
    }

    public List<RideEntity> findRidesByUserId(Long id) {
        return ridesRepository.findRidesByUserId(id);
    }

    @Override
    public RideEntity findRideById(Long id) {
        return ridesRepository.findRideById(id);
    }

    @Override
    public boolean save(RideEntity rideEntity) {
        ridesRepository.save(rideEntity);
        carService.setCarOnWay(rideEntity.getCar(), rideEntity.getStartPoint().getAddress());
        rideEntity.setStatus(RideStatus.RIDE_ASSIGNED_TO_DRIVER);
        //make maybe another timer for car to ride to passanger

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {

                      carService.setCarFree(rideEntity.getCar());
                      carService.changeCarLocation(rideEntity.getCar(), rideEntity.getDestination());
                      rideEntity.setStatus(RideStatus.RIDE_ENDED);

                    }
                },
                15000
        //ride.getRideTime().getTime()
        );
        long tm = rideEntity.getRideTime().getTime();
        return true;
    }

    @Override
    public RideFormDto createRide(RideFormDto rideFormDto,String userName) throws InterruptedException, ApiException, IOException {
        User user = userRepository.findByUserName(userName);

        double price = Precision.round(this.calculatePrice(rideFormDto.getOrigin(),rideFormDto.getDestination(), CarType.valueOf(rideFormDto.getCarType())),2);
        String timeOfRide = this.calculateTimeOfRide(rideFormDto.getOrigin(),rideFormDto.getDestination(), CarType.valueOf(rideFormDto.getCarType()));

        RideEntity ride = ridesRepository.save(RideEntity.builder()
                .rideTime(Time.valueOf(LocalTime.now()))
                .rideDate(rideFormDto.getDate())
                .startPoint(locationMapper.fromAddressToLocation(rideFormDto.getOrigin()))
                .destination(locationMapper.fromAddressToLocation(rideFormDto.getDestination()))
                .price(price)
                .rideTime(Time.valueOf(LocalTime.now()))
                .build());
        ridesRepository.save(ride);

        return rideFormDto;

    }

    @Override
    public void updateRideStatus(RideStatus status, Long rideId) {
        RideEntity rideEntity = ridesRepository.findRideById(rideId);
        rideEntity.setStatus(status);
        ridesRepository.save(rideEntity);
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
