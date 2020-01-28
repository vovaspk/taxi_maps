package com.taximaps.server.component.maps;

import com.google.maps.errors.ApiException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MapsApiFacadeImpl implements MapsApiFacade {
    @Override
    public double getDriveDistance(String startAddress, String endAddress) throws InterruptedException, ApiException, IOException {
        return JsonReader.getDriveDistance(startAddress, endAddress);
    }

    @Override
    public String getDriveTime(String startAddress, String endAddress) throws InterruptedException, ApiException, IOException {
        return JsonReader.getDriveTime(startAddress, endAddress);
    }

    @Override
    public String getGeocodeCoordinates(String address) {
        return JsonReader.getGeocodeCoordinates(address);
    }

    @Override
    public String getAddressLocationFromCoords(double lat, double lng) throws InterruptedException, ApiException, IOException {
        return JsonReader.getAddressLocationFromCoords(lat, lng);
    }
}
