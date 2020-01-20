package com.taximaps.server.entity.dto;

import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.Location;
import com.taximaps.server.entity.status.RideStatus;
import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
public class FullRideDto {
    private String rideTime;
    private String rideDate;
    private Location startPoint;
    private Location destination;
    private Car car;
    private RideStatus status;
    private double price;
}
