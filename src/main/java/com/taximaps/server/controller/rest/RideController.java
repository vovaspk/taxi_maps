package com.taximaps.server.controller.rest;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.RideEntity;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.FullRideDto;
import com.taximaps.server.entity.dto.RideFormDto;
import com.taximaps.server.service.RidesService;
import com.taximaps.server.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/rides", consumes = MediaType.APPLICATION_JSON_VALUE)
    public FullRideDto createRide(@RequestBody RideFormDto rideFormDto, Authentication authentication) throws InterruptedException, ApiException, IOException {
        String name = authentication.getName();
        return ridesService.saveRide(rideFormDto, name);
    }
    // TODO make one page for ride handling, remove cookies and send variables through js files


    @GetMapping("/rides/all")
    public List<RideEntity> getRidesList(HttpServletRequest req) {
        User user = getUser(req);
        return ridesService.findRidesByUserId(user.getId());

    }

    @GetMapping("/rides/{id}")
    public RideEntity getRide(Model model, @PathVariable Long id) {
        return ridesService.findRideById(id);
    }

    private User getUser(HttpServletRequest req) {
        String userName = req.getUserPrincipal().getName();
        return (User) userService.loadUserByUsername(userName);
    }

}
