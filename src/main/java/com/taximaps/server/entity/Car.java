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

    public Car(Long id, String name, Location location, CarStatus carStatus){
        super(id);
        this.name = name;
        this.location = location;
        this.carStatus = carStatus;
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                ", location=" + location +
                ", carStatus=" + carStatus +
                '}';
    }
}