package com.taximaps.server.mapper;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.RideEntity;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.ride.FullRideDto;
import com.taximaps.server.entity.dto.ride.RideFormDto;

import java.io.IOException;
import java.util.List;

public interface RideMapper {
    RideEntity toRideEntity(RideFormDto rideFormDto, Car car, User user) throws InterruptedException, ApiException, IOException;
    RideFormDto toRideDto (RideEntity rideEntity);
    List<RideFormDto> toRideDtos (List<RideEntity> rideEntities);
    FullRideDto toFullRideDto (RideEntity ride);
}
