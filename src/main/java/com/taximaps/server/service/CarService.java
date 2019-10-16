package com.taximaps.server.service;

import com.google.maps.model.LatLng;
import com.taximaps.server.domain.Car;
import com.taximaps.server.domain.status.CarStatus;

import java.util.List;

public interface CarService {
    List<Car> findAll();
    List<Car> findCarsByCarStatus(CarStatus carStatus);
    List<Car> findCarsByLocation(LatLng location);
}
