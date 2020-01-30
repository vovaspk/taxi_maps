package com.taximaps.server.entity.dto.ride;

import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.Location;
import com.taximaps.server.entity.status.RideStatus;
import lombok.Data;

@Data
public class FullRideDto {
    private String rideTimeStarted;
    private String rideDate;
    private Location startPoint;
    private Location destination;
    private Car car;
    private RideStatus status;
    private double price;
    private String waitingTime;
    private String timeOfRide;
}
