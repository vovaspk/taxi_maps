package com.taximaps.server.controller.rest;

import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.dto.car.FindCarDto;
import com.taximaps.server.entity.status.CarStatus;
import com.taximaps.server.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CarController {

    private CarService carService;


    @GetMapping("/cars/free")
    public List<Car> getFreeCars() {
        return carService.findCarsByCarStatus(CarStatus.FREE);
    }

    @GetMapping("/cars/find")
    public Car findNearestCarToLocation(@RequestBody FindCarDto findCarDto)  {
        return carService.findNearestCarToLocationAndType(findCarDto.getAddress(), findCarDto.getCarType());
    }

    @GetMapping("/cars/all")
    public List<Car> getAllCars(){
        return carService.findAll();
    }


}
