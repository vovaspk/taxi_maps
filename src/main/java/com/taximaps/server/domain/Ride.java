package com.taximaps.server.domain;

import com.google.maps.model.LatLng;
import com.taximaps.server.domain.status.RideStatus;

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
    @Enumerated(value = EnumType.STRING)
    private RideStatus status;
    @Enumerated(EnumType.STRING)
    private RideType rideType = RideType.ORDINARY;
    private double price;

    public Ride(Time rideTime, Date rideDate, LatLng startPoint, LatLng destination, User user, RideStatus status, RideType rideType, double price) {
        this.rideTime = rideTime;
        this.rideDate = rideDate;
        this.startPoint = startPoint;
        this.destination = destination;
        this.user = user;
        this.status = status;
        this.rideType = rideType;
        this.price = price;
    }



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
