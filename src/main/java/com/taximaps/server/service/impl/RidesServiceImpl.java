package com.taximaps.server.service.impl;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.RideEntity;
import com.taximaps.server.entity.dto.FullRideDto;
import com.taximaps.server.entity.dto.RideFormDto;
import com.taximaps.server.entity.status.RideStatus;
import com.taximaps.server.mapper.RideMapper;
import com.taximaps.server.maps.JsonReader;
import com.taximaps.server.repository.RidesRepository;
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

    private static final double PRICE_FOR_1_KM = 11;

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
    public FullRideDto saveRide(RideFormDto rideFormDto, String userName) throws InterruptedException, ApiException, IOException {
        rideFormDto.setCarType(rideFormDto.getCarType().toUpperCase());
        //TODO make class to return price and distance to reuse distance for time of ride
        //TODO startTime, finishTime, rideTime, rating(1-5)
        double price = this.roundPrice(rideFormDto);
        String timeOfRide = this.calculateTimeOfRide(rideFormDto.getOrigin(), rideFormDto.getDestination(), CarType.valueOf(rideFormDto.getCarType()));

        RideEntity ride = rideMapper.toRideEntity(rideFormDto, userName);
        ride.setPrice(price);
        ridesRepository.save(ride);


        carService.setCarOnWay(ride.getCar(), ride.getStartPoint().getAddress());
        updateRideStatus(RideStatus.RIDE_ASSIGNED_TO_DRIVER, ride.getId());

        startRide(ride);
        endRide(ride);

        return rideMapper.toFullRideDto(ride);

    }

    private void endRide(RideEntity ride) {
        new Timer().schedule(
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
    }

    private void startRide(RideEntity ride) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                updateRideStatus(RideStatus.RIDE_STARTED, ride.getId());
            }
        }, 15000);
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
    //TODO make rating menu after ride is ended button
    @Override
    public void updateRideRating(Long rideId, int rating) {
        RideEntity ride = ridesRepository.findRideById(rideId);
        ride.setRating(rating);
        ridesRepository.save(ride);
    }

    @Override
    public double calculatePrice(String origin, String dest, CarType carType) throws InterruptedException, ApiException, IOException {

        double distance = JsonReader.getDriveDistance(origin, dest);
        double price = (distance/1000) * PRICE_FOR_1_KM;

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
                price = (distance/1000) * PRICE_FOR_1_KM;
                break;
        }

        return price;
    }

    @Override
    public String calculateTimeOfRide(String origin, String dest, CarType carType) throws InterruptedException, ApiException, IOException {
        return JsonReader.getDriveTime(origin, dest);
    }

    @Override
    public String calculateTimeFromDriverToPassenger(String passengerLocation, String driverLocation, CarType carType) throws InterruptedException, ApiException, IOException {
        return JsonReader.getDriveTime(driverLocation, passengerLocation);
    }


}
