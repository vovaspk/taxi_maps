package com.taximaps.server.entity;

import com.google.maps.model.LatLng;
import com.taximaps.server.entity.status.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ride extends AbstractEntity{
    private Time rideTime;
    private Date rideDate;
    private LatLng startPoint;
    private LatLng destination;
    @OneToOne
    @JoinColumn(name="car_id")
    private Car car;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;
    @Enumerated(value = EnumType.STRING)
    private RideStatus status;
    @Enumerated(EnumType.STRING)
    private RideType rideType = RideType.ORDINARY;
    private double price;

    public Ride(Long id, Time rideTime, Date rideDate, LatLng startPoint, LatLng destination,
                Car car, User user, RideStatus status, RideType rideType, double price) {
        super(id);
        this.rideTime = rideTime;
        this.rideDate = rideDate;
        this.startPoint = startPoint;
        this.destination = destination;
        this.car = car;
        this.user = user;
        this.status = status;
        this.rideType = rideType;
        this.price = price;
    }
}
