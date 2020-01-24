package com.taximaps.server.mapper;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.RideEntity;
import com.taximaps.server.entity.dto.FullRideDto;
import com.taximaps.server.entity.dto.RideFormDto;

import java.io.IOException;
import java.util.List;

public interface RideMapper {
    RideEntity toRideEntity(RideFormDto rideFormDto, String userName) throws InterruptedException, ApiException, IOException;
    RideFormDto toRideDto (RideEntity rideEntity);
    List<RideFormDto> toRideDtos (List<RideEntity> rideEntities);
    FullRideDto toFullRideDto (RideEntity ride);
}
