package com.taximaps.server.controller.rest;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.RideEntity;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.RideFormDto;
import com.taximaps.server.mapper.LocationMapper;
import com.taximaps.server.service.RidesService;
import com.taximaps.server.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class RideController {

    private UserService userService;
    private RidesService ridesService;

    @PostMapping("/rides")
    public RideFormDto createRide(@RequestBody RideFormDto rideFormDto, Authentication authentication) throws InterruptedException, ApiException, IOException {
        String name = authentication.getName();
        return ridesService.createRide(rideFormDto, name);
    }


    @GetMapping("/rides/all")
    public List<RideEntity> getRidesList(HttpServletRequest req) {
        User user = getUser(req);
        List<RideEntity> userRideEntities = ridesService.findRidesByUserId(user.getId());

        return userRideEntities;
    }

    @GetMapping("/rides/{id}")
    public RideEntity getRide(Model model, @PathVariable Long id) {
        RideEntity rideEntity = ridesService.findRideById(id);

        return rideEntity;

    }

    private User getUser(HttpServletRequest req) {
        String userName = req.getUserPrincipal().getName();
        return (User) userService.loadUserByUsername(userName);
    }

}
