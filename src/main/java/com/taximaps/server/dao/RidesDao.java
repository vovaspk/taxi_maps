package com.taximaps.server.dao;

import com.taximaps.server.domain.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RidesDao extends JpaRepository<Ride, Long> {

    List<Ride> findAll();
    //List<Ride> findAllById(Iterable<Long> iterable);
    List<Ride> findRidesByUser(String user);

}
