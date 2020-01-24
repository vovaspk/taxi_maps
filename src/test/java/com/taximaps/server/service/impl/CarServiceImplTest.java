package com.taximaps.server.service.impl;


import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.Location;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.CarDto;
import com.taximaps.server.entity.status.CarStatus;
import com.taximaps.server.mapper.CarMapper;
import com.taximaps.server.mapper.impl.LocationMapperImpl;
import com.taximaps.server.repository.CarRepository;
import com.taximaps.server.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    private static final String CAR_NAME = "carName";
    private static final long CAR_ID = 1L;
    private static final String LOCATION = "location";
    private static final long USER_ID = 1L;
    private List<Car> CARS = new ArrayList<>();
    private static final Car CAR = new Car(CAR_ID, CAR_NAME, new Location(), CarStatus.FREE, CarType.ORDINARY, new User());

    private CarServiceImpl carService;
    @Mock
    private CarRepository carRepository;
    @Mock
    private LocationMapperImpl locationMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CarMapper carMapper;


    @BeforeEach
    void setUp() {
        carService = new CarServiceImpl(carRepository,locationMapper,userRepository, carMapper);
        Car car = new Car(CAR_ID, CAR_NAME, new Location(), CarStatus.FREE, CarType.ORDINARY, new User());
        CARS.add(car);
    }

    @Test
    void findAll() {
        carService.findAll();
        verify(carRepository).findAll();
    }

    @Test
    void whenFindAllShouldReturnCarsList(){
        when(carRepository.findAll()).thenReturn(CARS);

        List<Car> cars =carService.findAll();

        Assertions.assertEquals(cars, CARS);
    }

    @Test
    void findCarsByCarStatus() {
          carService.findCarsByCarStatus(CarStatus.FREE);
          verify(carRepository).findCarsByCarStatus(CarStatus.FREE);
    }

    @Test
    void findNearestCarToLocationAndType() {
        when(locationMapper.fromCoordsToLocation(LOCATION)).thenReturn(new Location());
        when(carRepository.findCarsByCarStatusAndCarType(CarStatus.FREE, CarType.ORDINARY)).thenReturn(CARS);

        carService.findNearestCarToLocationAndType(LOCATION, CarType.ORDINARY);

        verify(locationMapper).fromCoordsToLocation(LOCATION);
        verify(carRepository).findCarsByCarStatusAndCarType(CarStatus.FREE, CarType.ORDINARY);
    }

    @Test
    void setCarFree() {
        carService.setCarFree(CAR);
        verify(carRepository).save(CAR);
    }

    @Test
    void setCarOnWay() {
        carService.setCarOnWay(CAR, LOCATION);
        verify(carRepository).save(CAR);
    }

    @Test
    void setCarRiding() {
        carService.setCarRiding(CAR);
        verify(carRepository).save(CAR);
    }

    @Test
    void registerNewCar() {
        CarDto carDto = new CarDto();
        carDto.setName(CAR.getName());
        carDto.setLocation(CAR.getLocation().getAddress());
        carDto.setCarStatus(CAR.getCarStatus());
        carDto.setCarType(CAR.getCarType());
        carDto.setDriverId(CAR.getDriver().getId());

        when(carMapper.toCarEntity(carDto)).thenReturn(CAR);

        carService.registerNewCar(carDto);

        verify(carRepository).save(CAR);
    }

    @Test
    void changeCarLocation() {
        carService.changeCarLocation(CAR, new Location());
        verify(carRepository).save(CAR);
    }

    @Test
    void removeCar() {
        when(carRepository.getOne(CAR_ID)).thenReturn(CAR);

        carService.removeCar(CAR_ID);

        verify(carRepository).getOne(CAR_ID);
        verify(carRepository).delete(CAR);
    }

    @Test
    void assignDriverToCar() {
        when(userRepository.getOne(USER_ID)).thenReturn(new User());
        when(carRepository.getOne(CAR_ID)).thenReturn(CAR);

        carService.assignDriverToCar(USER_ID, CAR_ID);

        verify(carRepository).save(CAR);
    }
}