package com.taximaps.server.controller.rest;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.dto.FindCarDto;
import com.taximaps.server.entity.status.CarStatus;
import com.taximaps.server.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
    public Car findNearestCarToLocation(@RequestBody FindCarDto findCarDto) throws InterruptedException, ApiException, IOException {
        return carService.findNearestCarToLocationAndType(findCarDto.getAddress(), findCarDto.getCarType());
    }

    @GetMapping("/cars/all")
    public List<Car> getAllCars(){
        return carService.findAll();
    }


}
