package com.taximaps.server.controller.web;

import com.taximaps.server.entity.Ride;
import com.taximaps.server.entity.User;
import com.taximaps.server.mapper.LocationMapper;
import com.taximaps.server.service.RidesService;
import com.taximaps.server.service.UserService;
import com.taximaps.server.utils.pages.PagesConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@AllArgsConstructor
public class RidesController {

    private UserService userService;
    private RidesService ridesService;
    private LocationMapper locationMapper;

    @GetMapping("/rides")
    public String getRidesList(HttpServletRequest req, Model model) {
        User user = getUser(req);
        List<Ride> userRides = ridesService.findRidesByUserId(user.getId());
        //create form for ride shown in page to see start and dest in string, not it lat, lng
        //.stream()
               // .map(ride -> locationMapper.toAddressLocation(ride.getStartPoint().getLat(),
                 //                                             ride.getStartPoint().getLng()));
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

    private User getUser(HttpServletRequest req) {
        String userName = req.getUserPrincipal().getName();
        return (User) userService.loadUserByUsername(userName);
    }

}
