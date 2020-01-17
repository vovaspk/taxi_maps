package com.taximaps.server.service;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.Location;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.status.CarStatus;

import java.io.IOException;
import java.util.List;

public interface CarService {
    List<Car> findAll();
    List<String> getCarsInfo();
    List<Car> findCarsByCarStatus(CarStatus carStatus);
    Car findNearestCarToLocationAndType(String location, CarType carType) throws InterruptedException, ApiException, IOException;
    void setCarFree(Car car);
    void setCarOnWay(Car car, String address);
    void setCarRiding(Car car);
    Car registerNewCar(Car car);
    void changeCarLocation(Car car, Location location);
    void removeCar(Long id);
    void assignDriverToCar(Long userId, Long carId);
}
