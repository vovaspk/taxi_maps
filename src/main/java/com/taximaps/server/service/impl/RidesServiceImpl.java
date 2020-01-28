package com.taximaps.server.service.impl;

import com.google.maps.errors.ApiException;
import com.taximaps.server.component.maps.MapsApiFacade;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.RideEntity;
import com.taximaps.server.entity.dto.FullRideDto;
import com.taximaps.server.entity.dto.RideFormDto;
import com.taximaps.server.entity.status.RideStatus;
import com.taximaps.server.mapper.RideMapper;
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

    private static final int SCALE = 2;
    private static final int DELAY = 15000;
    private static final int PET_PRICE = 10;
    private static final int LUXURY_PRICE = 25;
    private static final int GROUP_PRICE = 15;
    private static final int KM1 = 1000;
    private static final double PRICE_FOR_1_KM = 11;

    private RidesRepository ridesRepository;
    private CarService carService;
    private RideMapper rideMapper;
    private MapsApiFacade mapsApiFacade;


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
        validateRideFormDto(rideFormDto);
        rideFormDto.setCarType(rideFormDto.getCarType().toUpperCase());
        //TODO make class to return price and distance to reuse distance for time of ride
        //TODO startTime, finishTime, rideTime, rating(1-5)
        double price = this.roundPrice(rideFormDto);
        //String timeOfRide = this.calculateTimeOfRide(rideFormDto.getOrigin(), rideFormDto.getDestination(), CarType.valueOf(rideFormDto.getCarType()));

        RideEntity ride = rideMapper.toRideEntity(rideFormDto, userName);
        ride.setPrice(price);
        ridesRepository.save(ride);


        carService.setCarOnWay(ride.getCar(), ride.getStartPoint().getAddress());
        updateRideStatus(RideStatus.RIDE_ASSIGNED_TO_DRIVER, ride.getId());

        startRide(ride);
        endRide(ride);

        return rideMapper.toFullRideDto(ride);

    }

    private void validateRideFormDto(RideFormDto rideFormDto) {
        if(rideFormDto.getOrigin() != null){

        }
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
                DELAY
        );
    }

    private void startRide(RideEntity ride) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                updateRideStatus(RideStatus.RIDE_STARTED, ride.getId());
            }
        }, DELAY);
    }

    public double roundPrice(RideFormDto rideFormDto) throws InterruptedException, ApiException, IOException {
        return Precision.round(this.calculatePrice(rideFormDto.getOrigin(), rideFormDto.getDestination(), CarType.valueOf(rideFormDto.getCarType())), SCALE);
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

        double distance = mapsApiFacade.getDriveDistance(origin, dest);
        double price = (distance/ KM1) * PRICE_FOR_1_KM;

        switch (carType){
            case PET:
                price += PET_PRICE;
            break;
            case LUXURY:
                price += LUXURY_PRICE;
                break;
            case GROUP:
                price += GROUP_PRICE;
                break;
            default:
                price = (distance/KM1) * PRICE_FOR_1_KM;
                break;
        }

        return price;
    }

    @Override
    public String calculateTimeOfRide(String origin, String dest, CarType carType) throws InterruptedException, ApiException, IOException {
        return mapsApiFacade.getDriveTime(origin, dest);
    }

    @Override
    public String calculateTimeFromDriverToPassenger(String passengerLocation, String driverLocation, CarType carType) throws InterruptedException, ApiException, IOException {
        return mapsApiFacade.getDriveTime(driverLocation, passengerLocation);
    }


}
