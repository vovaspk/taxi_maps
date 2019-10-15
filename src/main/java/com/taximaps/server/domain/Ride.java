package com.taximaps.server.domain;

import com.google.maps.model.LatLng;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Time rideTime;
    private Date rideDate;
    private LatLng startPoint;
    private LatLng destination;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    private RideStatus status;
    private double price;
//TODO rozibratys with this class to perform in database
// and maybe cut rideStatus from ride and add rideStatus to car
// and run add markers(drivers) randomly in city and find nearest and find his arrivaltime
// and think about flow between main ,find driver,  then save ride, and where to go
// and think about rideStatus resting, on his way to client, riding with client or without rideStatus
// if rideStatus will exists then think about checking the status of your driver or car when you book a ride
// and class car with status
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Time getRideTime() {
        return rideTime;
    }

    public void setRideTime(Time rideTime) {
        this.rideTime = rideTime;
    }

    public Date getRideDate() {
        return rideDate;
    }

    public void setRideDate(Date rideDate) {
        this.rideDate = rideDate;
    }

    public LatLng getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(LatLng startPoint) {
        this.startPoint = startPoint;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
