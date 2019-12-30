package com.taximaps.server.mapper.impl;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.RideEntity;
import com.taximaps.server.entity.User;
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


        RideEntity rideEntity = RideEntity.builder()
                .rideTime(Time.valueOf(LocalTime.now()))
                .rideDate(rideFormDto.getDate())
                .user(user)
                .startPoint(locationMapper.fromAddressToLocation(rideFormDto.getOrigin()))
                .destination(locationMapper.fromAddressToLocation(rideFormDto.getDestination()))
                .car(foundCar)
                .status(RideStatus.NEW_RIDE)
                .build();
        return rideEntity;
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
        List<RideFormDto> rideFormDtos = rideEntities.stream().map(this::toRideDto).collect(Collectors.toList());
        return rideFormDtos;
    }

    @Override
    public List<RideEntity> toRideEntites(List<RideFormDto> rideFormDtos) {
        List<RideEntity> rideEntityList = rideFormDtos.stream().map(rideFormDto -> {
            try {
                return toRideEntity(rideFormDto, "mocked user");
            } catch (InterruptedException | ApiException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        return rideEntityList;
    }
}