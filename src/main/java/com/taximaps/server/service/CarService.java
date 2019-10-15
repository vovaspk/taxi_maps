package com.taximaps.server.service;

import com.taximaps.server.domain.Car;

import java.util.List;

public interface CarService {
    List<Car> findAll();
    List<Car> findCarByRideStatus();
    List<Car> findCarByLocation();
}
