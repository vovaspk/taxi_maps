package com.taximaps.server.service.impl;

import com.google.maps.model.LatLng;
import com.taximaps.server.repository.CarRepository;
import com.taximaps.server.domain.Car;
import com.taximaps.server.domain.status.CarStatus;
import com.taximaps.server.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private CarRepository carRepository;

    @Autowired
    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }


    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public List<Car> findCarsByCarStatus(CarStatus carStatus) {
        return carRepository.findCarsByCarStatus(carStatus);
    }

    @Override
    public List<Car> findCarsByLocation(LatLng location) {
        return carRepository.findCarsByLocation(location);
    }
}
