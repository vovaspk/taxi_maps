package com.taximaps.server.service;

import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.Location;
import com.taximaps.server.entity.dto.car.CarDto;
import com.taximaps.server.entity.status.CarStatus;

import java.util.List;

public interface CarService {
    List<Car> findAll();
    List<Car> findCarsByCarStatus(CarStatus carStatus);
    Car findNearestCarToLocationAndType(String location, CarType carType);
    Car registerNewCar(CarDto car);
    void setCarFree(Car car);
    void setCarOnWay(Car car, String address);
    void setCarRiding(Car car);
    void changeCarLocation(Car car, Location location);
    void removeCar(Long id);
    void assignDriverToCar(Long userId, Long carId);
}
