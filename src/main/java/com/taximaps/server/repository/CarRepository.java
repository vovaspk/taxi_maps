package com.taximaps.server.repository;

import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.status.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car,Long> {
    List<Car> findAll();
    List<Car> findCarsByCarStatus(CarStatus carStatus);
    //Car findNearestCarToLocation(String location);
}
