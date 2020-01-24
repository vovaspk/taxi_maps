package com.taximaps.server.repository;

import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.ICarInfo;
import com.taximaps.server.entity.status.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car,Long> {
    Car findCarById(Long id);
    List<Car> findAll();
    List<Car> findCarsByCarStatus(CarStatus carStatus);
    List<Car> findCarsByCarStatusAndCarType(CarStatus carStatus, CarType carType);
    Integer countAllByCarStatus(CarStatus carStatus);
    @Query(value = "select c.id as id, c.name as name, l.address as address from car as c join location as l on c.location_id = l.id", nativeQuery = true)
    List<ICarInfo> getAllCarsInfo();
}
