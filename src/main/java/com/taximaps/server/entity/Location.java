package com.taximaps.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "location")
public class Location extends AbstractEntity{

    @Column(name = "lat", nullable = false)
    private double lat;
    @Column(name = "lng", nullable = false)
    private double lng;
    @Column(name = "address", nullable = false)
    private String address;

    public Location(Long id, double lat, double lng, String address){
        super(id);
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    @Override
    public String toString() {
        return lat + "," + lng + ", " + address;
    }
}
