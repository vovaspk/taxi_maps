package com.taximaps.server.dao;

import com.taximaps.server.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarDao extends JpaRepository<Car,Long> {
    List<Car> findAll();
    List<Car> findCarByRideStatus();
    List<Car> findCarByLocation();
}
