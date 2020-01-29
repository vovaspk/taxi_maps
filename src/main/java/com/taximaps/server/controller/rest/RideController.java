package com.taximaps.server.controller.rest;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.RideEntity;
import com.taximaps.server.entity.dto.ride.FullRideDto;
import com.taximaps.server.entity.dto.ride.FullRideInfoDto;
import com.taximaps.server.entity.dto.ride.RideFormDto;
import com.taximaps.server.service.RidesService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class RideController {

    private RidesService ridesService;

    @PostMapping(value = "/rides", consumes = MediaType.APPLICATION_JSON_VALUE)
    public FullRideDto createRide(@RequestBody @Valid RideFormDto rideFormDto, Authentication auth) throws InterruptedException, ApiException, IOException {
        return ridesService.bookRide(rideFormDto, auth.getName());
    }

    @GetMapping("/rides/all")
    public List<RideEntity> getUserRidesList(Authentication auth) {
        return ridesService.findUserRides(auth.getName());
    }

    @GetMapping("/rides/{id}")
    public RideEntity getRide(Model model, @PathVariable Long id) {
        return ridesService.findRideById(id);
    }

    @PostMapping("/rides/info")
    public FullRideInfoDto getInfoBeforeRide(@RequestBody @Valid RideFormDto rideFormDto) throws InterruptedException, ApiException, IOException {
        return ridesService.getRideInfoBeforeRide(rideFormDto);
    }

}
