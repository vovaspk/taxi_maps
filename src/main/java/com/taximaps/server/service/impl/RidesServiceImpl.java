package com.taximaps.server.service.impl;

import com.taximaps.server.repository.RidesRepository;
import com.taximaps.server.domain.Ride;
import com.taximaps.server.service.RidesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RidesServiceImpl implements RidesService {

    @Autowired
    private RidesRepository ridesRepository;

    public List<Ride> findAll(){
        return ridesRepository.findAll();
    }
    public List<Ride> findRidesByUserId(Long id){
        return ridesRepository.findRidesByUserId(id);
    }

    @Override
    public Ride findRideById(Long id) {
        return ridesRepository.findRideById(id);
    }
}
