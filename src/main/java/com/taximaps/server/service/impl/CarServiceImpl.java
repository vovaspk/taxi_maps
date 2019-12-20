package com.taximaps.server.service.impl;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.Location;
import com.taximaps.server.entity.status.CarStatus;
import com.taximaps.server.mapper.LocationMapper;
import com.taximaps.server.repository.CarRepository;
import com.taximaps.server.service.CarService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class CarServiceImpl implements CarService {

    private CarRepository carRepository;
    private LocationMapper locationMapper;

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public List<String> getCarsInfo() {
        List<String> carsInfo = new ArrayList<>();

        return carsInfo;
    }

    @Override
    public List<Car> findCarsByCarStatus(CarStatus carStatus) {
        return carRepository.findCarsByCarStatus(carStatus);
    }

    @Override
    public Car findNearestCarToLocationAndType(String location, CarType carType) throws InterruptedException, ApiException, IOException {
        Location givenLocation = locationMapper.fromCoordsToLocation(location);
        List<Car> availableCars = carRepository.findCarsByCarStatusAndCarType(CarStatus.FREE, carType);

        Map<Car, Double> carDoubleMap = new HashMap<>();
        for (Car tempCar : availableCars) {
            carDoubleMap.put(tempCar, distance(tempCar.getLocation().getLat(), tempCar.getLocation().getLng(), givenLocation.getLat(), givenLocation.getLng()));
        }

        Car foundCar = Collections.min(carDoubleMap.entrySet(), Map.Entry.comparingByValue()).getKey();
        log.info("found nearest car to location: ", foundCar.getLocation().getAddress());
//        return availableCars
//                .stream()
//                .sorted()
//                .min((car1, car2) -> distance(givenLocation.getLat(), givenLocation.getLng(),car1.getLocation().getLat(), car1.getLocation().getLng()))
//                .get();
        return foundCar;
    }


    @Override
    public void setCarFree(Car car) {
        car.setCarStatus(CarStatus.FREE);
        carRepository.save(car);
        log.info("car is free: ", car);
    }

    @Override
    public void setCarOnWay(Car car, String address) {
        car.setCarStatus(CarStatus.ONWAY);
        carRepository.save(car);
        log.info("car is on way to: ", car, address);
    }

    @Override
    public void setCarRiding(Car car) {
        car.setCarStatus(CarStatus.RIDING);
        carRepository.save(car);
        log.info("car is riding: ", car);
    }

    @Override
    public void save(Car car) {
        carRepository.save(car);
    }

    @Override
    public void changeCarLocation(Car car, Location location) {
        car.setLocation(location);
        carRepository.save(car);
        log.info("car is now here: ", location.getAddress());
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
