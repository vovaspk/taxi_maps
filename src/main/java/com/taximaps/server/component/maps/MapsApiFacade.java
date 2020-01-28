package com.taximaps.server.component.maps;

import com.google.maps.errors.ApiException;

import java.io.IOException;

public interface MapsApiFacade {

    double getDriveDistance(String startAddress, String endAddress) throws InterruptedException, ApiException, IOException;

    String getDriveTime(String startAddress, String endAddress) throws InterruptedException, ApiException, IOException;

    String getGeocodeCoordinates(String address);

    String getAddressLocationFromCoords(double lat, double lng) throws InterruptedException, ApiException, IOException;
}
