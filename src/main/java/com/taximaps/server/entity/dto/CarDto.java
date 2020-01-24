package com.taximaps.server.entity.dto;

import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.status.CarStatus;
import lombok.Data;

@Data
public class CarDto {
    private String name;
    private String location;
    private CarStatus carStatus;
    private CarType carType;
    private Long driverId;
}
