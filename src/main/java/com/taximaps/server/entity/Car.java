package com.taximaps.server.entity;

import com.taximaps.server.entity.status.CarStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.taximaps.server.entity.CarType.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Car extends AbstractEntity{

    private String name;
    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @Enumerated(EnumType.STRING)
    private CarStatus carStatus;
    @Enumerated(EnumType.STRING)
    private CarType carType = ORDINARY;
    @OneToOne
    @JoinTable(name = "car_driver",
            joinColumns = {@JoinColumn(name = "car_id")},
            inverseJoinColumns = {@JoinColumn(name = "driver_id")})
    private User driver;

    public Car(Long id, String name, Location location, CarStatus carStatus, CarType carType, User driver){
        super(id);
        this.name = name;
        this.location = location;
        this.carStatus = carStatus;
        this.carType = carType;
        this.driver = driver;
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                ", location=" + location +
                ", carStatus=" + carStatus +
                ", carType=" + carType +
                '}';
    }
}
