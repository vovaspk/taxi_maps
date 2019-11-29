package com.taximaps.server.service;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.status.CarStatus;

import java.io.IOException;
import java.util.List;

public interface CarService {
    List<Car> findAll();
    List<Car> findCarsByCarStatus(CarStatus carStatus);
    Car findNearestCarToLocation(String location) throws InterruptedException, ApiException, IOException;
    void setCarFree(Car car);
    void save(Car car);
}
