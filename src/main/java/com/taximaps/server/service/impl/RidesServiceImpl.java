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
    public List<Ride> findRidesByUser(String userName){
        return ridesDao.findRidesByUser(userName);
    }
}
