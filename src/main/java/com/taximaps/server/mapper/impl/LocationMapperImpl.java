package com.taximaps.server.mapper.impl;

import com.google.maps.errors.ApiException;
import com.taximaps.server.component.maps.MapsApiFacade;
import com.taximaps.server.entity.Location;
import com.taximaps.server.mapper.LocationMapper;
import com.taximaps.server.repository.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class LocationMapperImpl implements LocationMapper {

    private LocationRepository locationRepository;
    private MapsApiFacade mapsApiFacade;

    @Override
    public String toStringLocation(double lat, double lng) {
        Location location = new Location();
        String address = toAddressLocation(lat, lng);
        location.setLat(lat);
        location.setLng(lng);
        location.setAddress(address);
        return location.toString();
    }

    @Override
    public String toAddressLocation(double lat, double lng)  {
        try {
            return mapsApiFacade.getAddressLocationFromCoords(lat, lng);
        } catch (InterruptedException | ApiException | IOException e) {
            e.printStackTrace();
        }
        return "Cannot find address for this location";
    }

    //check if coords already exists in db then get from db end return it
    @Override
    public Location fromCoordsToLocation(String coords) {
        List<Location> availableLocations = locationRepository.findAll();

        String convertedLocation = mapsApiFacade.getGeocodeCoordinates(coords);

        String lat = convertedLocation.split(",")[0];
        String lng = convertedLocation.split(",")[1];

        Location locationIfFound = new Location();
        for(Location location : availableLocations){
            if(isSameLocation(coords, lat, lng, location)){
                return location;
            }
        }
        locationIfFound.setLat(Double.parseDouble(lat));
        locationIfFound.setLng(Double.parseDouble(lng));
        locationIfFound.setAddress(coords);
        locationRepository.save(locationIfFound);
        return locationIfFound;
    }

    private boolean isSameLocation(String coords, String lat, String lng, Location location) {
        return location.getAddress().equalsIgnoreCase(coords) || (location.getLat() == Double.parseDouble(lat) && location.getLng() == Double.parseDouble(lng));
    }

}
