package com.taximaps.server.entity.dto.car;

import com.taximaps.server.entity.CarType;
import lombok.Data;

@Data
public class FindCarDto {
    String address;
    CarType carType;
}
