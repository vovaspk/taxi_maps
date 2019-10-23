package com.taximaps.server.repository;

import com.taximaps.server.domain.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RidesRepository extends JpaRepository<Ride, Long> {

    List<Ride> findAll();
    Ride findRideById(Long id);
    List<Ride> findRidesByUserId(Long id);

}
