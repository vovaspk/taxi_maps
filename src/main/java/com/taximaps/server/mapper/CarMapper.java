package com.taximaps.server.mapper;

import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.dto.car.CarDto;

public interface CarMapper {
    Car toCarEntity(CarDto carDto);
    CarDto toCarDto(Car car);
}
