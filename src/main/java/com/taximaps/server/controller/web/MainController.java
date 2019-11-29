package com.taximaps.server.controller.web;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.*;
import com.taximaps.server.entity.dto.UserProfileFormDto;
import com.taximaps.server.service.CarService;
import com.taximaps.server.utils.CarCoordinatsUtils;
import com.taximaps.server.service.RidesService;
import com.taximaps.server.service.UserService;
import com.taximaps.server.service.impl.RidesServiceImpl;
import com.taximaps.server.service.impl.UserServiceImpl;
import com.taximaps.server.utils.pages.PagesConstants;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.taximaps.server.maps.JsonReader.*;

@Controller
@AllArgsConstructor
public class MainController {

    private UserService userService;
    private RidesService ridesService;
    private CarService carService;


    @GetMapping(value = {"/", "/map", "/main"}, produces = "text/html")
    public String map(Model model) {
        return PagesConstants.MAIN_PAGE;
    }

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

    @GetMapping("/user/profile")
    public String profile(Model model, HttpServletRequest req) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserProfileFormDto formDto = new UserProfileFormDto();
        String username = ((UserDetails) principal).getUsername();

        System.out.println("current user: " + username);
        User user = getUser(req);
        model.addAttribute("formDto", formDto);
        model.addAttribute("name", user.getUserName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("password", user.getPassword());
        return PagesConstants.PROFILE_PAGE;
    }

    @PostMapping(value = "/processInput", produces = "text/html")
    public String getOriginAndDestFromUser(@RequestParam String origin, @RequestParam String destination, @RequestParam String rideType, Date date, Model model, HttpServletRequest req) throws IOException, ApiException, InterruptedException {
        model.addAttribute("origin", origin);
        model.addAttribute("destination", destination);
        model.addAttribute("cars", carService.findAll());
        model.addAttribute("carLocations", CarCoordinatsUtils.getCoords());

        String originPlaceId = getGeocodeCoordinats(origin);
        String destPlaceId = getGeocodeCoordinats(destination);
        model.addAttribute("originPlaceId", originPlaceId);
        model.addAttribute("destPlaceId", destPlaceId);

        RideType rideType1 = RideType.valueOf(rideType.toUpperCase());


        User user = getUser(req);
        double price = ridesService.calculatePrice(origin,destination, rideType1);
        String timeOfRide = ridesService.calculateTime(origin, destination, rideType1);
        model.addAttribute("timeOfRide", timeOfRide);
        model.addAttribute("price", price);
        model.addAttribute("testPrice", price);

        System.out.println("Time: " + timeOfRide);
        System.out.println("price: " + price);
//        ridesService.save(
//                new RideBuilder()
//                        .setRideTime(Time.valueOf(LocalTime.now()))
//                        .setRideDate(date)
//                        .setStartPoint(originCoords)
//                        .setDestination(destCoords)
        //make method find nearest car (from available cars) and assign car to ride and make it non available
        Car foundCar = carService.findNearestCarToLocation(destination);
        model.addAttribute("foundCarCoords", foundCar.getLocation().toString());
        System.out.println(foundCar.toString());
//                        .setCar(foundCar);
//                        .setUser(user)
//                        .setStatus(RideStatus.NEW_RIDE)
//                        .setRideType(rideType)
//                        .setPrice(price)
//                        .createRide());

        return PagesConstants.RESPONSE_PAGE;

    }

    private User getUser(HttpServletRequest req) {
        String userName = req.getUserPrincipal().getName();
        return (User) userService.loadUserByUsername(userName);
    }

    @GetMapping("/getDist")
    public String getDist(String startAddress, String endAddress, Model model) throws InterruptedException, ApiException, IOException {
        model.addAttribute("dist", getDriveDistance(startAddress, endAddress));
        return PagesConstants.RESPONSE_PAGE;
    }

    @GetMapping("/getDirect")
    public String getDirect(String startAddress, String endAddress, Model model) throws InterruptedException, ApiException, IOException {
        String directionJson = getDirectionResult(startAddress, endAddress);
        model.addAttribute("start", startAddress);
        model.addAttribute("end", endAddress);
        model.addAttribute("direct", directionJson);
        return PagesConstants.RESPONSE_PAGE;
    }


}
