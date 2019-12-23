package com.taximaps.server.controller.rest;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.status.CarStatus;
import com.taximaps.server.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller()
@AllArgsConstructor
public class CarController {

    private CarService carService;


    @GetMapping("/cars/all")
    @ResponseBody
    public List<Car> getCars(){
        return carService.findCarsByCarStatus(CarStatus.FREE);
    }

    @GetMapping("/cars/find")
    @ResponseBody
    public Car findNearestCarToLocation(String address, CarType carType) throws InterruptedException, ApiException, IOException {
        return carService.findNearestCarToLocationAndType(address, carType);
    }
}
