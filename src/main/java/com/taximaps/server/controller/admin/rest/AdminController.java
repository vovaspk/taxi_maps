package com.taximaps.server.controller.admin.rest;

import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.dto.car.CarDto;
import com.taximaps.server.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AdminController {

    private CarService carService;

    @PostMapping("/admin")
    public Car addCar(CarDto car) {
        return carService.registerNewCar(car);
    }

    @DeleteMapping("/admin")
    public void removeCar(Long carId){
         carService.removeCar(carId);
    }

    @PostMapping("/admin/assignDriver")
    public void assignDriverToCar(Long userId, Long carId){
        carService.assignDriverToCar(userId, carId);
    }

}
