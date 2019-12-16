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

import static com.taximaps.server.maps.JsonReader.getGeocodeCoordinats;

@Component
@AllArgsConstructor
public class LocationMapperImpl implements LocationMapper {

    private JsonReader jsonReader;
    private LocationRepository locationRepository;

    @Override
    public String toStringLocation(double lat, double lng) throws InterruptedException, ApiException, IOException {
        Location location = new Location();
        String address = toAddressLocation(lat, lng);
        location.setLat(lat);
        location.setLng(lng);
        location.setAddress(address);
        return location.toString();
    }

    @Override
    public String toAddressLocation(double lat, double lng) throws InterruptedException, ApiException, IOException {
        return JsonReader.getAddressLocationFromCoords(lat, lng);
    }

    //check if coords already exists in db then get from db end return it
    @Override
    public Location fromCoordsToLocation(String coords) throws InterruptedException, ApiException, IOException {
        List<Location> availableLocations = locationRepository.findAll();

        String convertedLocation = getGeocodeCoordinats(coords);

        String lat = convertedLocation.split(",")[0];
        String lng = convertedLocation.split(",")[1];
        String address = toAddressLocation(Double.parseDouble(lat), Double.parseDouble(lng));

        Location locationIfFound = new Location();
        for(Location location : availableLocations){
            if(location.getAddress().equalsIgnoreCase(address)){
                return location;
            }
        }
        locationIfFound.setLat(Double.parseDouble(lat));
        locationIfFound.setLng(Double.parseDouble(lng));
        locationIfFound.setAddress(address);
        locationRepository.save(locationIfFound);
        return locationIfFound;
    }

    @Override
    public Location fromAddressToLocation(String location) throws InterruptedException, ApiException, IOException {

        String geocodeCoordinats = getGeocodeCoordinats(location);

        return fromCoordsToLocation(geocodeCoordinats);
    }
}
