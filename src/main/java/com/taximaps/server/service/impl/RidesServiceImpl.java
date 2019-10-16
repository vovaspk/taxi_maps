package com.taximaps.server.service.impl;

import com.taximaps.server.dao.RidesDao;
import com.taximaps.server.domain.Ride;
import com.taximaps.server.service.RidesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RidesServiceImpl implements RidesService {

    @Autowired
    private RidesDao ridesDao;

    public List<Ride> findAll(){
        return ridesDao.findAll();
    }
    public List<Ride> findRidesByUserId(Long id){
        return ridesDao.findRidesByUserId(id);
    }

    @Override
    public Ride findRideById(Long id) {
        return ridesDao.findRideById(id);
    }
}
