package com.taximaps.server.service.impl;

import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.Location;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.car.CarDto;
import com.taximaps.server.entity.status.CarStatus;
import com.taximaps.server.mapper.CarMapper;
import com.taximaps.server.mapper.LocationMapper;
import com.taximaps.server.repository.CarRepository;
import com.taximaps.server.repository.UserRepository;
import com.taximaps.server.service.CarService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class CarServiceImpl implements CarService {

    private CarRepository carRepository;
    private LocationMapper locationMapper;
    private UserRepository userRepository;
    private CarMapper carMapper;

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public List<Car> findCarsByCarStatus(CarStatus carStatus) {
        return carRepository.findCarsByCarStatus(carStatus);
    }

    @Override
    public Car findNearestCarToLocationAndType(String location, CarType carType) {
        Location givenLocation = locationMapper.fromCoordsToLocation(location);
        List<Car> availableCars = carRepository.findCarsByCarStatusAndCarType(CarStatus.FREE, carType);

        Map<Car, Double> carDoubleMap = new HashMap<>();
        for (Car tempCar : availableCars) {
            carDoubleMap.put(tempCar, getDistance(givenLocation, tempCar));
        }

        Car foundCar = Collections.min(carDoubleMap.entrySet(), Map.Entry.comparingByValue()).getKey();
        log.info("found nearest car to location: {}", foundCar.getLocation().getAddress());
        return foundCar;
    }


    @Override
    public void setCarFree(Car car) {
        car.setCarStatus(CarStatus.FREE);
        carRepository.save(car);
        log.info("car {} is free: ", car);
    }

    @Override
    public void setCarOnWay(Car car, String address) {
        car.setCarStatus(CarStatus.ONWAY);
        carRepository.save(car);
        log.info("car {} is on way to: {}", car.getName(), address);
    }

    @Override
    public void setCarRiding(Car car) {
        car.setCarStatus(CarStatus.RIDING);
        carRepository.save(car);
        log.info("car {} is riding: ", car.getName());
    }

    @Override
    public Car registerNewCar(CarDto car) {
        Car carToSave = carMapper.toCarEntity(car);
        return carRepository.save(carToSave);
    }

    @Override
    public void changeCarLocation(Car car, Location location) {
        car.setLocation(location);
        carRepository.save(car);
        log.info("car is now here: {}", car.getLocation().getAddress());
    }

    @Override
    public void removeCar(Long id) {
        Car car = carRepository.getOne(id);
        if(carIsBusy(car)){
            log.warn("trying to delete car {}, but car is riding now", car);
        }
        carRepository.delete(car);
    }

    @Override
    public void assignDriverToCar(Long userId, Long carId) {
        User user = userRepository.getOne(userId);
        Car car = carRepository.getOne(carId);
        car.setDriver(user);
        carRepository.save(car);
    }

    private double getDistance(Location givenLocation, Car tempCar) {
        return distance(tempCar.getLocation().getLat(), tempCar.getLocation().getLng(), givenLocation.getLat(), givenLocation.getLng());
    }

    private boolean carIsBusy(Car car){
        return car.getCarStatus().equals(CarStatus.ONWAY) || car.getCarStatus().equals(CarStatus.RIDING);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        // haversine great circle distance approximation, returns meters
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60; // 60 nautical miles per degree of seperation
        dist = dist * 1852; // 1852 meters per nautical mile
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
