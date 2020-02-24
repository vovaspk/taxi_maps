package com.taximaps.server.service.impl;

import com.google.maps.errors.ApiException;
import com.taximaps.server.component.maps.MapsApiFacade;
import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.RideEntity;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.ride.FullRideDto;
import com.taximaps.server.entity.dto.ride.FullRideInfoDto;
import com.taximaps.server.entity.dto.ride.RideFormDto;
import com.taximaps.server.entity.status.RideStatus;
import com.taximaps.server.mapper.RideMapper;
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
    private UserRepository userRepository;

    public List<RideEntity> findAll() {
        return ridesRepository.findAll();
    }

    public List<RideEntity> findUserRides(String userName) {
        User user = userRepository.findByUserName(userName);
        return ridesRepository.findRidesByUserId(user.getId());
    }

    @Override
    public RideEntity findRideById(Long id) {
        return ridesRepository.findRideById(id);
    }

    //TODO startTime, finishTime, rideTime, rating(1-5)
    @Override
    public FullRideDto bookRide(RideFormDto rideFormDto, String userName) throws InterruptedException, ApiException, IOException {
        User user = userRepository.findByUserName(userName);
        rideFormDto.setCarType(rideFormDto.getCarType().toUpperCase());

        double distance = getDistance(rideFormDto.getOrigin(), rideFormDto.getDestination());
        double price = this.roundPrice(distance, CarType.valueOf(rideFormDto.getCarType()));
        Car foundCar = carService.findNearestCarToLocationAndType(rideFormDto.getOrigin(), CarType.valueOf(rideFormDto.getCarType()));
        String timeOfRide = this.calculateTimeOfRide(rideFormDto.getOrigin(), rideFormDto.getDestination(), CarType.valueOf(rideFormDto.getCarType()));
        String waitingTime = calculateTimeFromDriverToPassenger(rideFormDto.getOrigin(), foundCar.getLocation().getAddress(), CarType.valueOf(rideFormDto.getCarType()));

        RideEntity ride = rideMapper.toRideEntity(rideFormDto,foundCar, user);
        ride.setPrice(price);
        ridesRepository.save(ride);

        carService.setCarOnWay(ride.getCar(), ride.getStartPoint().getAddress());
        updateRideStatus(RideStatus.RIDE_ASSIGNED_TO_DRIVER, ride.getId());

        startRide(ride);
        endRide(ride);

        FullRideDto fullRideDto = rideMapper.toFullRideDto(ride);
        fullRideDto.setTimeOfRide(timeOfRide);
        fullRideDto.setWaitingTime(waitingTime);
        return fullRideDto;

    }

    @Override
    public FullRideInfoDto getRideInfoBeforeRide(RideFormDto rideFormDto) throws InterruptedException, ApiException, IOException {
        rideFormDto.setCarType(rideFormDto.getCarType().toUpperCase());
        double rideDistance = getDistance(rideFormDto.getOrigin(), rideFormDto.getDestination());
        double price = calculatePrice(rideDistance,CarType.valueOf(rideFormDto.getCarType()));

        String timeOfRide = calculateTimeOfRide(rideFormDto.getOrigin(), rideFormDto.getDestination(), CarType.valueOf(rideFormDto.getCarType()));

        FullRideInfoDto infoDto = new FullRideInfoDto();
        infoDto.setStartPoint(rideFormDto.getOrigin());
        infoDto.setDestination(rideFormDto.getDestination());
        infoDto.setTimeOfRide(timeOfRide);
        infoDto.setPrice(price);
        return infoDto;
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
    public double calculatePrice(double distance, CarType carType) {

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
    public double getDistance(String origin, String dest) throws InterruptedException, ApiException, IOException {
        return mapsApiFacade.getDriveDistance(origin, dest);
    }

    @Override
    public String calculateTimeOfRide(String origin, String dest, CarType carType) throws InterruptedException, ApiException, IOException {
        return mapsApiFacade.getDriveTime(origin, dest);
    }

    @Override
    public String calculateTimeFromDriverToPassenger(String passengerLocation, String driverLocation, CarType carType) throws InterruptedException, ApiException, IOException {
        return mapsApiFacade.getDriveTime(driverLocation, passengerLocation);
    }

    public double roundPrice(double distance, CarType carType) {
        return Precision.round(calculatePrice(distance, carType), SCALE);
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

}
