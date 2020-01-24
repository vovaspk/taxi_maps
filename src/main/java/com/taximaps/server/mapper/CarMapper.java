package com.taximaps.server.mapper;

import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.dto.CarDto;

public interface CarMapper {
    Car toCarEntity(CarDto carDto);
    CarDto toCarDto(Car car);
}
