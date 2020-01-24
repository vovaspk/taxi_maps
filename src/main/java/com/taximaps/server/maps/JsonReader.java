package com.taximaps.server.maps;

import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class JsonReader {

    private static final String API_KEY = "AIzaSyBR4RT24iLNmr74yE168KRWz5qdjr3NhVc";

    public static double getDriveDistance(String startAddress, String endAddress) throws InterruptedException, ApiException, IOException {
        DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(getGeoContext());
        DistanceMatrix result = req.origins(startAddress)
                .destinations(endAddress)
                .mode(TravelMode.DRIVING)
                .avoid(DirectionsApi.RouteRestriction.TOLLS)
                .language("en-US")
                .await();

        return result.rows[0].elements[0].distance.inMeters;

    }

    public static String getDriveTime(String startAddress, String endAddress) throws InterruptedException, ApiException, IOException {
        DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(getGeoContext());
        DistanceMatrix result = req.origins(startAddress)
                .destinations(endAddress)
                .mode(TravelMode.DRIVING)
                .avoid(DirectionsApi.RouteRestriction.TOLLS)
                .language("en-US")
                .await();

        return result.rows[0].elements[0].duration.toString();

    }

    public static String getGeocodeCoordinates(String address)  {
        GeocodingApiRequest request = GeocodingApi.newRequest(getGeoContext()).address(address);
        GeocodingResult result = null;
        try {
            result = request.await()[0];
        } catch (ApiException | InterruptedException | IOException e) {
           log.error("cannot get geocode coordinats for addressL {}", address);
        }
        return result.geometry.location.toString();

    }

    public static String getAddressLocationFromCoords(double lat, double lng) throws InterruptedException, ApiException, IOException {
        GeocodingApiRequest request = GeocodingApi.reverseGeocode(getGeoContext(), new LatLng(lat, lng));
        GeocodingResult result = request.await()[0];
        return result.formattedAddress;
    }

    private static GeoApiContext getGeoContext() {
        return new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

    }

}
