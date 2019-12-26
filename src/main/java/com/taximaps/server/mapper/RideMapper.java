package com.taximaps.server.mapper;

import com.taximaps.server.entity.RideEntity;
import com.taximaps.server.entity.dto.RideFormDto;

import java.util.List;

public interface RideMapper {
    RideEntity toRide (RideFormDto rideFormDto);
    RideFormDto toRideDto (RideEntity rideEntity);
    List<RideFormDto> toRideDtos (List<RideEntity> rideEntities);
    List<RideEntity> toRideEntites (List<RideEntity> rideEntities);
}
