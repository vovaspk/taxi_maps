package com.taximaps.server.mapper.impl;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.RideEntity;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.FullRideDto;
import com.taximaps.server.entity.dto.RideFormDto;
import com.taximaps.server.entity.status.RideStatus;
import com.taximaps.server.mapper.LocationMapper;
import com.taximaps.server.mapper.RideMapper;
import com.taximaps.server.repository.UserRepository;
import com.taximaps.server.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.taximaps.server.entity.RideEntity.*;

@Component
@AllArgsConstructor
public class RideMapperImpl implements RideMapper {

    private UserRepository userRepository;
    private CarService carService;
    private LocationMapper locationMapper;


    @Override
    public RideEntity toRideEntity(RideFormDto rideFormDto, String userName) throws InterruptedException, ApiException, IOException {

        User user = userRepository.findByUserName(userName);
        Car foundCar = carService.findNearestCarToLocationAndType(rideFormDto.getOrigin(), CarType.valueOf(rideFormDto.getCarType()));


        return builder()
                .rideTime(Time.valueOf(LocalTime.now()))
                .rideDate(rideFormDto.getDate())
                .user(user)
                .startPoint(locationMapper.fromCoordsToLocation(rideFormDto.getOrigin()))
                .destination(locationMapper.fromCoordsToLocation(rideFormDto.getDestination()))
                .car(foundCar)
                .status(RideStatus.NEW_RIDE)
                .build();
    }

    @Override
    public RideFormDto toRideDto(RideEntity rideEntity) {
        RideFormDto rideFormDto = new RideFormDto();
        rideFormDto.setOrigin(rideEntity.getStartPoint().getAddress());
        rideFormDto.setDestination(rideEntity.getDestination().getAddress());
        rideFormDto.setCarType(rideEntity.getCar().getCarType().name());

        return rideFormDto;
    }

    @Override
    public List<RideFormDto> toRideDtos(List<RideEntity> rideEntities) {
       return rideEntities.stream().map(this::toRideDto).collect(Collectors.toList());
    }

    @Override
    public FullRideDto toFullRideDto(RideEntity ride) {
        FullRideDto fullRideDto = new FullRideDto();
        fullRideDto.setRideTime(ride.getRideTime().toString());
        fullRideDto.setRideDate(ride.getRideDate().toString());
        fullRideDto.setStartPoint(ride.getStartPoint());
        fullRideDto.setDestination(ride.getDestination());
        fullRideDto.setCar(ride.getCar());
        fullRideDto.setStatus(ride.getStatus());
        fullRideDto.setPrice(ride.getPrice());
        return fullRideDto;
    }
}
