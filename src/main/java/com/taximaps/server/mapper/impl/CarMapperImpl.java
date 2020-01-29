package com.taximaps.server.mapper.impl;

import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.dto.car.CarDto;
import com.taximaps.server.mapper.CarMapper;
import com.taximaps.server.mapper.LocationMapper;
import com.taximaps.server.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CarMapperImpl implements CarMapper {

    private UserService userService;
    private LocationMapper locationMapper;

    @Override
    public Car toCarEntity(CarDto carDto) {
        Car car = new Car();
        car.setName(carDto.getName());
        car.setLocation(locationMapper.fromCoordsToLocation(carDto.getLocation()));
        car.setCarStatus(carDto.getCarStatus());
        car.setCarType(carDto.getCarType());
        car.setDriver(userService.getUserById(carDto.getDriverId()));
        return car;
    }

    @Override
    public CarDto toCarDto(Car car) {
        CarDto carDto = new CarDto();
        carDto.setName(car.getName());
        carDto.setLocation(car.getLocation().getAddress());
        carDto.setCarStatus(car.getCarStatus());
        carDto.setCarType(car.getCarType());
        carDto.setDriverId(car.getDriver().getId());
        return carDto;
    }
}
