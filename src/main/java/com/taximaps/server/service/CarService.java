package com.taximaps.server.service;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.status.CarStatus;

import java.io.IOException;
import java.util.List;

public interface CarService {
    List<Car> findAll();
    List<Car> findCarsByCarStatus(CarStatus carStatus);
    Car findNearestCarToLocationAndType(String location, CarType carType) throws InterruptedException, ApiException, IOException;
    void setCarFree(Car car);
    void setCarOnWay(Car car);
    void setCarRiding(Car car);
    void save(Car car);
}
