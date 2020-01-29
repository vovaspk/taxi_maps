package com.taximaps.server.service.impl;

import com.google.maps.errors.ApiException;
import com.taximaps.server.component.maps.MapsApiFacadeImpl;
import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.Location;
import com.taximaps.server.entity.RideEntity;
import com.taximaps.server.entity.Role;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.ride.FullRideDto;
import com.taximaps.server.entity.dto.ride.FullRideInfoDto;
import com.taximaps.server.entity.dto.ride.RideFormDto;
import com.taximaps.server.entity.status.CarStatus;
import com.taximaps.server.entity.status.RideStatus;
import com.taximaps.server.mapper.impl.RideMapperImpl;
import com.taximaps.server.repository.RidesRepository;
import com.taximaps.server.repository.UserRepository;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RidesServiceImplTest {

    private static final int RATING = 5;
    private static final Long USER_ID = 1L;
    private static final Long RIDE_ID = 1L;
    private static final double RIDE_DISTANCE = 2005;
    private static final String USER_NAME = "USERNAME";
    private static final String ORIGIN_ADDRESS = "ORIGIN_ADDRESS";
    private static final String DESTINATION_ADDRESS = "DESTINATION_ADDRESS";
    private static final String ORDINARY = "ORDINARY";
    private static final double PRICE = 120.0;
    private static final double ROUNDED_PRICE = 22.06;
    private static final double NOT_ROUNDED_PRICE = 22.055;
    private static final String USER_EMAIL = "user@gmail.com";
    private static final String USER_PASSWORD = "1234";
    private static final String WAITING_TIME = "12";
    private static final double CAR_LNG = 28.123421;
    private static final double CAR_LAT = 42.216812;
    private static final Long CAR_ID = 1L;
    private static final String CAR_NAME = "Car_Name";
    private static final Long LOCATION_ID = 1L;
    private static final String RIDE_TIME = "12";
    private static final double LUXURY_PRICE = 47.055;

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
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        ridesService = new RidesServiceImpl(ridesRepository,carService,rideMapper, mapsApiFacade, userRepository);
        RideEntity rideEntity = new RideEntity(Time.valueOf(LocalTime.now()), new Date(), new Location(),new Location(), getCar(), getUser(), RideStatus.NEW_RIDE, PRICE, RATING);
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
        when(userRepository.findByUserName(USER_NAME)).thenReturn(getUser());
        ridesService.findUserRides(USER_NAME);
        verify(ridesRepository).findRidesByUserId(USER_ID);
    }

    @Test
    void testFindRideById() {
        ridesService.findRideById(USER_ID);
        verify(ridesRepository).findRideById(USER_ID);
    }

    @Test
    void saveRide() throws InterruptedException, ApiException, IOException {
        //given
        RideFormDto rideFormDto = getRideFormDto();
        Car foundCar = getCar();
        RideEntity rideEntity = RIDES.get(0);
        //when
        when(userRepository.findByUserName(USER_NAME)).thenReturn(getUser());
        when(rideMapper.toRideEntity(rideFormDto,foundCar, getUser())).thenReturn(rideEntity);
        when(carService.findNearestCarToLocationAndType(rideFormDto.getOrigin(),CarType.ORDINARY)).thenReturn(foundCar);
        when(ridesService.calculateTimeFromDriverToPassenger(foundCar.getLocation().getAddress(), rideFormDto.getOrigin(),CarType.ORDINARY)).thenReturn(WAITING_TIME);
        when(ridesRepository.findRideById(rideEntity.getId())).thenReturn(rideEntity);
        when(rideMapper.toFullRideDto(rideEntity)).thenReturn(getFullRideDto());
        //then
        ridesService.bookRide(rideFormDto, USER_NAME);
        verify(ridesRepository, times(2)).save(rideEntity);
    }

    @Test
    public void getRideInfoBeforeRide() throws InterruptedException, ApiException, IOException {
        //given
        RideFormDto rideFormDto = getRideFormDto();
        //when
        when(mapsApiFacade.getDriveDistance(rideFormDto.getOrigin(),rideFormDto.getDestination())).thenReturn(RIDE_DISTANCE);
        when(mapsApiFacade.getDriveTime(rideFormDto.getOrigin(), rideFormDto.getDestination())).thenReturn(RIDE_TIME);

        FullRideInfoDto rideInfoBeforeRide = ridesService.getRideInfoBeforeRide(rideFormDto);
        //then
        Assertions.assertEquals(ORIGIN_ADDRESS, rideInfoBeforeRide.getStartPoint());
        Assertions.assertEquals(DESTINATION_ADDRESS, rideInfoBeforeRide.getDestination());
        Assertions.assertEquals(NOT_ROUNDED_PRICE, rideInfoBeforeRide.getPrice());
        Assertions.assertEquals(RIDE_TIME, rideInfoBeforeRide.getTimeOfRide());
    }

    @Test
    void roundPrice() {
        double roundPrice = ridesService.roundPrice(RIDE_DISTANCE,CarType.ORDINARY);
        Assertions.assertEquals(ROUNDED_PRICE, roundPrice);
    }

    @Test
    void updateRideStatus() {
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
    void calculateOrdinaryPrice() {
        double price = ridesService.calculatePrice(RIDE_DISTANCE, CarType.ORDINARY);
        Assertions.assertEquals(NOT_ROUNDED_PRICE, price);
    }

    @Test
    void calculateLuxuryPrice() {
        double price = ridesService.calculatePrice(RIDE_DISTANCE, CarType.LUXURY);
        Assertions.assertEquals(LUXURY_PRICE, price);
    }

    @Test
    void calculateTimeOfRide() throws InterruptedException, ApiException, IOException {
        ridesService.calculateTimeOfRide(ORIGIN_ADDRESS, DESTINATION_ADDRESS, CarType.ORDINARY);
        verify(mapsApiFacade).getDriveTime(ORIGIN_ADDRESS,DESTINATION_ADDRESS);
    }

    @Test
    void calculateTimeFromDriverToPassenger() throws InterruptedException, ApiException, IOException {
        ridesService.calculateTimeFromDriverToPassenger(ORIGIN_ADDRESS, DESTINATION_ADDRESS, CarType.ORDINARY);
        verify(mapsApiFacade).getDriveTime(DESTINATION_ADDRESS,ORIGIN_ADDRESS);
    }

    private FullRideDto getFullRideDto() {
        FullRideDto rideDto = new FullRideDto();
        rideDto.setCar(getCar());
        rideDto.setDestination(new Location());
        rideDto.setPrice(PRICE);
        rideDto.setWaitingTime(WAITING_TIME);
        rideDto.setRideTimeStarted(RIDE_TIME);
        return rideDto;
    }

    private Car getCar() {
        Location location = new Location();
        location.setId(LOCATION_ID);
        location.setAddress(DESTINATION_ADDRESS);
        location.setLng(CAR_LNG);
        location.setLat(CAR_LAT);

        Car car = new Car();
        car.setId(CAR_ID);
        car.setDriver(getUser());
        car.setName(CAR_NAME);
        car.setCarStatus(CarStatus.FREE);
        car.setLocation(location);
        return car;
    }

    private RideFormDto getRideFormDto() {
        RideFormDto rideFormDto = new RideFormDto();
        rideFormDto.setOrigin(ORIGIN_ADDRESS);
        rideFormDto.setDestination(DESTINATION_ADDRESS);
        rideFormDto.setCarType(ORDINARY);
        rideFormDto.setDate(new Date());
        return rideFormDto;

    }

    private User getUser(){
        User user = new User();
        user.setId(USER_ID);
        user.setUserName(USER_NAME);
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        return user;
    }
}