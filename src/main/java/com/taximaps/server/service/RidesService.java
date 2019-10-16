package com.taximaps.server.service;

import com.taximaps.server.domain.Ride;

import java.util.List;

public interface RidesService {
    List<Ride> findAll();
    List<Ride> findRidesByUserId(Long id);
    Ride findRideById(Long id);
}
