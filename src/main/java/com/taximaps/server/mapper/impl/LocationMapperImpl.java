package com.taximaps.server.mapper.impl;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.Location;
import com.taximaps.server.mapper.LocationMapper;
import com.taximaps.server.maps.JsonReader;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class LocationMapperImpl implements LocationMapper {

    private JsonReader jsonReader;

    @Override
    public String toStringLocation(double lat, double lng) {
        Location location = new Location();
        location.setLat(lat);
        location.setLng(lng);
        return location.toString();
    }

    @Override
    public Location toLocation(String location) throws InterruptedException, ApiException, IOException {
        String convertedLocation = JsonReader.getGeocodeCoordinats(location);
        Location location1 = new Location();
        String lat = convertedLocation.split(",")[0];
        String lng = convertedLocation.split(",")[1];
        location1.setLng(Double.valueOf(lng));
        location1.setLat(Double.valueOf(lat));
        return location1;
    }
}
