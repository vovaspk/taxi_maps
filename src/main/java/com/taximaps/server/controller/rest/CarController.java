package com.taximaps.server.controller.rest;

import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.status.CarStatus;
import com.taximaps.server.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller()
public class CarController {

    @Autowired
    private CarRepository carRepository;

    @GetMapping("/cars/all")
    @ResponseBody
    public List<Car> getCars(){
        return carRepository.findCarsByCarStatus(CarStatus.FREE);
    }
}
