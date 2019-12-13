package com.taximaps.server.mapper.impl;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.Location;
import com.taximaps.server.mapper.LocationMapper;
import com.taximaps.server.maps.JsonReader;
import com.taximaps.server.repository.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.taximaps.server.maps.JsonReader.getGeocodeCoordinats;

@Component
@AllArgsConstructor
public class LocationMapperImpl implements LocationMapper {

    private JsonReader jsonReader;
    private LocationRepository locationRepository;

    @Override
    public String toStringLocation(double lat, double lng) {
        Location location = new Location();
        location.setLat(lat);
        location.setLng(lng);
        return location.toString();
    }
//check if coords already exists in db then get from db end return it
    @Override
    public Location fromCoordsToLocation(String coords) throws InterruptedException, ApiException, IOException {
        List<Location> availableLocations = locationRepository.findAll();

        String convertedLocation = getGeocodeCoordinats(coords);

        String lat = convertedLocation.split(",")[0];
        String lng = convertedLocation.split(",")[1];
        Location locationIfFound =  availableLocations.stream()
                .skip(8)
                .filter(location -> location.getLat() == Double.parseDouble(lat)
                     && location.getLat() == Double.parseDouble(lng))
                .findFirst().get();
        if(locationIfFound != null){
            return locationIfFound;
        }
        Location location1 = locationRepository.findLocationByLatContainsAndLngContains(lat, lng);
        if (location1 != null) {

            location1.setLng(Double.parseDouble(lng));
            location1.setLat(Double.parseDouble(lat));
            return location1;
        }else
            location1 = new Location();
        location1.setLng(Double.parseDouble(lng));
        location1.setLat(Double.parseDouble(lat));
        locationRepository.save(location1);
        return location1;
    }

    @Override
    public Location fromAddressToLocation(String location) throws InterruptedException, ApiException, IOException {

        String geocodeCoordinats = getGeocodeCoordinats(location);

        return fromCoordsToLocation(geocodeCoordinats);
    }
}
