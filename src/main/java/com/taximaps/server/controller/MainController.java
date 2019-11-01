package com.taximaps.server.controller;

import com.google.maps.errors.ApiException;
import com.taximaps.server.service.CarService;
import com.taximaps.server.utils.CarCoordinatsUtils;
import com.taximaps.server.domain.Ride;
import com.taximaps.server.domain.User;
import com.taximaps.server.service.RidesService;
import com.taximaps.server.service.UserService;
import com.taximaps.server.service.impl.RidesServiceImpl;
import com.taximaps.server.service.impl.UserServiceImpl;
import com.taximaps.server.utils.pages.PagesConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.taximaps.server.maps.JsonReader.*;

@Controller
public class MainController {

    private UserService userService;
    private RidesService ridesService;
    private CarService carService;

    @Autowired
    public MainController(UserServiceImpl userService, RidesServiceImpl ridesService, CarService carService) {
        this.userService = userService;
        this.ridesService = ridesService;
        this.carService = carService;
    }

    @GetMapping(value = {"/","/map", "/main"}, produces = "text/html")
    public String map (Model model){
        return PagesConstants.MAIN_PAGE;
    }

    @GetMapping("/rides")
    public String getRidesList(User user, Model model){
        List<Ride> userRides = ridesService.findRidesByUserId(user.getId());
        model.addAttribute("userRides", userRides);
        return PagesConstants.RIDES_PAGE;
    }

    //this will be page of specific ride
    @GetMapping("/rides/{id}")
    public String getRide(Model model, @PathVariable Long id){
        Ride ride = ridesService.findRideById(id);
        model.addAttribute("ride", ride);
        return PagesConstants.SPECIFIC_RIDE_PAGE;

    }

    @GetMapping("/user/profile")
    public String profile(Model model, /*@RequestParam String name,*/ HttpServletRequest req){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();


            String username = ((UserDetails)principal).getUsername();

            //String username = principal.toString();

        System.out.println("current user: " + username);
        String userName = req.getUserPrincipal().getName();
        User user = (User) userService.loadUserByUsername(userName);
        model.addAttribute("name", user.getUserName());
        return PagesConstants.PROFILE_PAGE;
    }

    @PostMapping(value = "/processInput", produces = "text/html")
    public String getOriginAndDestFromUser(@RequestParam String origin, @RequestParam String destination, Model model) throws IOException, ApiException, InterruptedException {
       // String response = JsonReader.sendRequest(origin, destination);
        model.addAttribute("origin", origin);
        model.addAttribute("destination", destination);
        model.addAttribute("cars", carService.findAll());
        model.addAttribute("carLocations", CarCoordinatsUtils.getCoords());

        String originPlaceId = getGeocodeCoordinats(origin);
        String destPlaceId = getGeocodeCoordinats(destination);
        model.addAttribute("originPlaceId", originPlaceId);
        model.addAttribute("destPlaceId", destPlaceId);
        //draw route on map in html
        //return the html page with map and route on it

        return PagesConstants.RESPONSE_PAGE;

    }

    @GetMapping("/getDist")
    public String getDist(String startAddress, String endAddress, Model model) throws InterruptedException, ApiException, IOException {
        model.addAttribute("dist",getDriveDistanceAndTime(startAddress,endAddress));
        return PagesConstants.RESPONSE_PAGE;
    }

    @GetMapping("/getDirect")
    public String getDirect(String startAddress, String endAddress, Model model) throws InterruptedException, ApiException, IOException {
        String directionJson = getDirectionResult(startAddress,endAddress);
        model.addAttribute("start", startAddress);
        model.addAttribute("end", endAddress);
        model.addAttribute("direct", directionJson);
        return PagesConstants.RESPONSE_PAGE;
    }





}
