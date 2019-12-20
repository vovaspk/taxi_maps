package com.taximaps.server.entity;

import com.taximaps.server.entity.status.CarStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private CarType carType = com.taximaps.server.entity.CarType.ORDINARY;

    public Car(Long id, String name, Location location, CarStatus carStatus, CarType carType){
        super(id);
        this.name = name;
        this.location = location;
        this.carStatus = carStatus;
        this.carType = carType;
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
