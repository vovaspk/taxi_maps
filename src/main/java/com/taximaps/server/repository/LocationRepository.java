package com.taximaps.server.repository;

import com.taximaps.server.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = "select * from location\n" +
            " where cast(location.lat as char(20)) like concat('%', ?1, '%') and cast(location.lng as char(20)) like concat('%', ?2, '%')", nativeQuery = true)
    Location findLocationByLatContainsAndLngContains(@Param("Lat") String lat, @Param("Lng") String lng);


}
