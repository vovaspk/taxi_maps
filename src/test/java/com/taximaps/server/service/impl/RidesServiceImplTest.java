package com.taximaps.server.service.impl;

import com.google.maps.errors.ApiException;
import com.taximaps.server.component.maps.MapsApiFacadeImpl;
import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.Location;
import com.taximaps.server.entity.RideEntity;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.RideFormDto;
import com.taximaps.server.entity.status.RideStatus;
import com.taximaps.server.mapper.impl.RideMapperImpl;
import com.taximaps.server.repository.RidesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RidesServiceImplTest {

    private static final double PRICE = 120.0;
    private static final int RATING = 5;
    private static final Long USER_ID = 1L;
    private static final Long RIDE_ID = 1L;
    private static final double RIDE_DISTANCE = 25;
    private static final String USER_NAME = "USERNAME";
    private static final String ORIGIN_ADDRESS = "ORIGIN_ADDRESS";
    private static final String DESTINATION_ADDRESS = "DESTINATION_ADDRESS";
    private static final String ORDINARY = "ORDINARY";
    private static final double ROUNDED_PRICE = 1.32;
    private List<RideEntity> RIDES = new ArrayList<>();
    private RidesServiceImpl ridesService;
    @Mock
    private RidesRepository ridesRepository;
    @Mock
    private CarServiceImpl carService;
    @Mock
    private RideMapperImpl rideMapper;
    @Mock
    private MapsApiFacadeImpl mapsApiFacade;

    @BeforeEach
    public void setUp() {
        ridesService = new RidesServiceImpl(ridesRepository,carService,rideMapper, mapsApiFacade);
        RideEntity rideEntity = new RideEntity(Time.valueOf(LocalTime.now()), new Date(), new Location(),new Location(), new Car(), new User(), RideStatus.NEW_RIDE, PRICE, RATING);
        rideEntity.setId(RIDE_ID);
        RIDES.add(rideEntity);
    }


    @Test
    void testFindAll() {
        ridesService.findAll();
        verify(ridesRepository).findAll();
    }

    @Test
    void testFindRidesByUserId() {
        ridesService.findRidesByUserId(USER_ID);
        verify(ridesRepository).findRidesByUserId(USER_ID);
    }

    @Test
    void testFindRideById() {
        ridesService.findRideById(USER_ID);
        verify(ridesRepository).findRideById(USER_ID);
    }

    @Test
    void saveRide() throws InterruptedException, ApiException, IOException {
        RideFormDto rideFormDto = getRideFormDto();
        RideEntity rideEntity = RIDES.get(0);

        when(rideMapper.toRideEntity(rideFormDto, USER_NAME)).thenReturn(rideEntity);
        when(ridesRepository.findRideById(rideEntity.getId())).thenReturn(rideEntity);

        ridesService.saveRide(rideFormDto, USER_NAME);
        verify(ridesRepository, times(2)).save(rideEntity);
    }

    @Test
    void roundPrice() throws InterruptedException, ApiException, IOException {
        when(mapsApiFacade.getDriveDistance(ORIGIN_ADDRESS, DESTINATION_ADDRESS)).thenReturn(PRICE);
        when(ridesService.calculatePrice(ORIGIN_ADDRESS, DESTINATION_ADDRESS, CarType.ORDINARY)).thenReturn(PRICE);
        double roundPrice = ridesService.roundPrice(getRideFormDto());
        Assertions.assertEquals(ROUNDED_PRICE, roundPrice);
    }

    @Test
    void updateRideStatus() throws InterruptedException, ApiException, IOException {
        when(ridesRepository.findRideById(RIDE_ID)).thenReturn(RIDES.get(0));
        ridesService.updateRideStatus(RideStatus.RIDE_ASSIGNED_TO_DRIVER, RIDE_ID);
        verify(ridesRepository).save(any());
    }

    @Test
    void updateRideRating() {
        when(ridesRepository.findRideById(RIDE_ID)).thenReturn(RIDES.get(0));
        ridesService.updateRideRating(RIDE_ID, RATING);
        verify(ridesRepository).save(any());
    }

    @Test
    void calculatePrice() {

    }

    @Test
    void calculateTimeOfRide() {
    }

    @Test
    void calculateTimeFromDriverToPassenger() {
    }

    private RideFormDto getRideFormDto() {
        RideFormDto rideFormDto = new RideFormDto();
        rideFormDto.setOrigin(ORIGIN_ADDRESS);
        rideFormDto.setDestination(DESTINATION_ADDRESS);
        rideFormDto.setCarType(ORDINARY);
        rideFormDto.setDate(new Date());
        return rideFormDto;

    }
}