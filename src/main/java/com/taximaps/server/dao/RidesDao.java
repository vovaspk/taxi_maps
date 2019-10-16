package com.taximaps.server.dao;

import com.taximaps.server.domain.Ride;
import com.taximaps.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RidesDao extends JpaRepository<Ride, Long> {

    List<Ride> findAll();
    Ride findRideById(Long id);
    List<Ride> findRidesByUserId(Long id);

}
