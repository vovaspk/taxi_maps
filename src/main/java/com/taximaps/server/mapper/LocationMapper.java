package com.taximaps.server.mapper;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.Location;

import java.io.IOException;

public interface LocationMapper {
    String toStringLocation(double lat, double lng);
    Location fromCoordsToLocation(String location) throws InterruptedException, ApiException, IOException;
    Location fromAddressToLocation(String location) throws InterruptedException, ApiException, IOException;
}
