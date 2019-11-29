package com.taximaps.server.utils;

import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CarCoordinatsUtils {
    private static ArrayList<String> coords = new ArrayList(Arrays.asList(
            "49.2259516,28.4052628",
            "49.2251249,28.4079674",
            "49.2256954,28.4404962",
            "49.2327425,28.4530055",
            "49.2327425,28.4530055",
            "49.2375411,28.4510661",
            "49.227373,28.3980087",
            "49.2289307,28.4230224"));

    private static LatLng[] latLngsCoords = {
            new LatLng(49.2259516, 28.4052628),
            new LatLng(49.2251249, 28.4079674),
            new LatLng(49.2256954, 28.4404962),
            new LatLng(49.2327425, 28.4530055),
            new LatLng(49.2375411, 28.4510661),
            new LatLng(49.227373, 28.3980087),
            new LatLng(49.2289307, 28.4230224)
    };

    public static List<String> getCoords() {
        return coords;
    }

    public static LatLng[] getLatLngsCoords() {
        return latLngsCoords;
    }
}
