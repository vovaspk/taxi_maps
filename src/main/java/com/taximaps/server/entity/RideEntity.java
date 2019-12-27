package com.taximaps.server.entity;

import com.taximaps.server.entity.status.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ride")
public class RideEntity extends AbstractEntity{
    private Time rideTime;
    private Date rideDate;
    @ManyToOne(/*cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}*/)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "start_location_id")
    private Location startPoint;
    @ManyToOne(/*cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}*/)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "dest_location_id")
    private Location destination;
    @OneToOne
    @JoinColumn(name="car_id")
    private Car car;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;
    @Enumerated(value = EnumType.STRING)
    private RideStatus status;
    private double price;

    public RideEntity(Long id, Time rideTime, Date rideDate, Location startPoint, Location destination,
                      Car car, User user, RideStatus status, double price) {
        super(id);
        this.rideTime = rideTime;
        this.rideDate = rideDate;
        this.startPoint = startPoint;
        this.destination = destination;
        this.car = car;
        this.user = user;
        this.status = status;
        this.price = price;
    }
}
