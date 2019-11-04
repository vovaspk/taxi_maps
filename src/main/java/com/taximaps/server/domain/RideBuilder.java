package com.taximaps.server.domain;

import com.google.maps.model.LatLng;
import com.taximaps.server.domain.status.RideStatus;

import java.sql.Time;
import java.util.Date;

public class RideBuilder {
    private Time rideTime;
    private Date rideDate;
    private LatLng startPoint;
    private LatLng destination;
    private User user;
    private RideStatus status;
    private RideType rideType;
    private double price;

    public RideBuilder setRideTime(Time rideTime) {
        this.rideTime = rideTime;
        return this;
    }

    public RideBuilder setRideDate(Date rideDate) {
        this.rideDate = rideDate;
        return this;
    }

    public RideBuilder setStartPoint(LatLng startPoint) {
        this.startPoint = startPoint;
        return this;
    }

    public RideBuilder setDestination(LatLng destination) {
        this.destination = destination;
        return this;
    }

    public RideBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public RideBuilder setStatus(RideStatus status) {
        this.status = status;
        return this;
    }

    public RideBuilder setRideType(RideType rideType) {
        this.rideType = rideType;
        return this;
    }

    public RideBuilder setPrice(double price) {
        this.price = price;
        return this;
    }

    public Ride createRide() {
        return new Ride(rideTime, rideDate, startPoint, destination, user, status, rideType, price);
    }
}