package com.taximaps.server.controller.web;

import com.taximaps.server.entity.Ride;
import com.taximaps.server.entity.User;
import com.taximaps.server.service.RidesService;
import com.taximaps.server.utils.pages.PagesConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@AllArgsConstructor
public class RidesController {

    private RidesService ridesService;

    @GetMapping("/rides")
    public String getRidesList(User user, Model model) {
        List<Ride> userRides = ridesService.findRidesByUserId(user.getId());
        model.addAttribute("userRides", userRides);
        return PagesConstants.RIDES_PAGE;
    }

    //this will be page of specific ride
    @GetMapping("/rides/{id}")
    public String getRide(Model model, @PathVariable Long id) {
        Ride ride = ridesService.findRideById(id);
        model.addAttribute("ride", ride);
        return PagesConstants.SPECIFIC_RIDE_PAGE;

    }

}
