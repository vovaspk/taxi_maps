package com.taximaps.server.service.impl;

import com.taximaps.server.dao.CarDao;
import com.taximaps.server.domain.Car;
import com.taximaps.server.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private CarDao carDao;

    @Autowired
    public CarServiceImpl(CarDao carDao) {
        this.carDao = carDao;
    }


    @Override
    public List<Car> findAll() {
        return carDao.findAll();
    }

    @Override
    public List<Car> findCarByRideStatus() {
        return carDao.findCarByRideStatus();
    }

    @Override
    public List<Car> findCarByLocation() {
        return carDao.findCarByLocation();
    }
}
