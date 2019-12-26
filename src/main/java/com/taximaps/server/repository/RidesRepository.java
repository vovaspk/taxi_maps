package com.taximaps.server.repository;

import com.taximaps.server.entity.RideEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RidesRepository extends JpaRepository<RideEntity, Long> {

    List<RideEntity> findAll();
    RideEntity findRideById(Long id);
    List<RideEntity> findRidesByUserId(Long id);

}
