package com.taximaps.server.service.impl;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.RideEntity;
import com.taximaps.server.entity.dto.RideFormDto;
import com.taximaps.server.entity.status.RideStatus;
import com.taximaps.server.mapper.LocationMapper;
import com.taximaps.server.mapper.RideMapper;
import com.taximaps.server.maps.JsonReader;
import com.taximaps.server.repository.RidesRepository;
import com.taximaps.server.repository.UserRepository;
import com.taximaps.server.service.CarService;
import com.taximaps.server.service.RidesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
@AllArgsConstructor
@Slf4j
public class RidesServiceImpl implements RidesService {

    private RidesRepository ridesRepository;
    private CarService carService;
    private RideMapper rideMapper;

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
                new TimerTask() {
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
    public RideFormDto createRide(RideFormDto rideFormDto, String userName) throws InterruptedException, ApiException, IOException {

        double price = this.roundPrice(rideFormDto);
        String timeOfRide = this.calculateTimeOfRide(rideFormDto.getOrigin(), rideFormDto.getDestination(), CarType.valueOf(rideFormDto.getCarType()));
        RideEntity ride = rideMapper.toRideEntity(rideFormDto, userName);
        ride.setPrice(price);
       //TODO think where to put time of ride
        ridesRepository.save(ride);

        carService.setCarOnWay(ride.getCar(), ride.getStartPoint().getAddress());
        updateRideStatus(RideStatus.RIDE_ASSIGNED_TO_DRIVER, ride.getId());
        //time for car to ride to passenger
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                updateRideStatus(RideStatus.RIDE_STARTED, ride.getId());
            }
        }, 15000);

        new java.util.Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        carService.setCarFree(ride.getCar());
                        carService.changeCarLocation(ride.getCar(), ride.getDestination());
                        updateRideStatus(RideStatus.RIDE_ENDED, ride.getId());
                    }
                },
                15000
        );
        return rideFormDto;

    }

    public double roundPrice(RideFormDto rideFormDto) throws InterruptedException, ApiException, IOException {
        return Precision.round(this.calculatePrice(rideFormDto.getOrigin(), rideFormDto.getDestination(), CarType.valueOf(rideFormDto.getCarType())), 2);
    }

    @Override
    public void updateRideStatus(RideStatus status, Long rideId) {
        RideEntity rideEntity = ridesRepository.findRideById(rideId);
        rideEntity.setStatus(status);
        ridesRepository.save(rideEntity);
    }

    @Override
    public double calculatePrice(String origin, String dest, CarType carType) throws InterruptedException, ApiException, IOException {

        double distance = JsonReader.getDriveDistance(origin, dest);
        double price = (distance/1000) * priceFor1KM;

        switch (carType){
            case PET:
                price += 10;
            break;
            case LUXURY:
                price += 25;
                break;
            case GROUP:
                price += 15;
                break;
            default:
                price = (distance/1000) * priceFor1KM;
                break;
        }

        return price;
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
