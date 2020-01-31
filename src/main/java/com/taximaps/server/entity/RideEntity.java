package com.taximaps.server.entity;

import com.taximaps.server.entity.status.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Time;
import java.util.Date;

import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ride")
public class RideEntity extends AbstractEntity{
    private Time rideTime;
    private Date rideDate;
    @ManyToOne
    @Cascade(SAVE_UPDATE)
    @JoinColumn(name = "start_location_id")
    private Location startPoint;
    @ManyToOne
    @Cascade(SAVE_UPDATE)
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
    private int rating;

}
