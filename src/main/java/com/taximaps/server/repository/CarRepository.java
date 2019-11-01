package com.taximaps.server.repository;

import com.google.maps.model.LatLng;
import com.taximaps.server.domain.Car;
import com.taximaps.server.domain.status.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car,Long> {
    List<Car> findAll();
    List<Car> findCarsByCarStatus(CarStatus carStatus);
    List<Car> findCarsByLocation(LatLng location);
}