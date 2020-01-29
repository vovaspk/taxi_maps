package com.taximaps.server.entity.dto.ride;

import lombok.Data;

@Data
public class FullRideInfoDto {
    private String startPoint;
    private String destination;
    private double price;
    private String timeOfRide;
}
