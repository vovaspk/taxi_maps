package com.taximaps.server.entity.dto;

import com.taximaps.server.entity.CarType;
import lombok.Data;

@Data
public class FindCarDto {
    String address;
    CarType carType;
}
