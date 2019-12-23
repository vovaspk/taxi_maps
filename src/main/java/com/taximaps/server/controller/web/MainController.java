package com.taximaps.server.controller.web;

import com.google.maps.errors.ApiException;
import com.taximaps.server.entity.Car;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.Ride;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.UserProfileFormDto;
import com.taximaps.server.entity.status.RideStatus;
import com.taximaps.server.mapper.LocationMapper;
import com.taximaps.server.service.CarService;
import com.taximaps.server.service.RidesService;
import com.taximaps.server.service.UserService;
import com.taximaps.server.utils.CarCoordinatsUtils;
import com.taximaps.server.utils.pages.PagesConstants;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Precision;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Locale;

import static com.taximaps.server.maps.JsonReader.*;

@Controller
@AllArgsConstructor
public class MainController {

    private UserService userService;
    private RidesService ridesService;
    private CarService carService;
    private LocationMapper locationMapper;


    @GetMapping(value = {"/", "/map", "/main"}, produces = "text/html")
    public String map(Model model) {
        return PagesConstants.MAIN_PAGE;
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
    public String processInput(@RequestParam String origin, @RequestParam String destination, @RequestParam String carType, @RequestParam String date, Model model, HttpServletRequest req) throws IOException, ApiException, InterruptedException, ParseException {
        model.addAttribute("origin", origin);
        model.addAttribute("destination", destination);
        model.addAttribute("cars", carService.findAll());
        model.addAttribute("carLocations", CarCoordinatsUtils.getCoords());

        String originPlaceId = getGeocodeCoordinats(origin);
        String destPlaceId = getGeocodeCoordinats(destination);
        model.addAttribute("originPlaceId", originPlaceId);
        model.addAttribute("destPlaceId", destPlaceId);

        CarType carType1 = CarType.valueOf(carType.toUpperCase());
        User user = getUser(req);

        double price = Precision.round(ridesService.calculatePrice(origin,destination, carType1),2);
        String timeOfRide = ridesService.calculateTimeOfRide(origin, destination, carType1);

        model.addAttribute("timeOfRide", timeOfRide);
        model.addAttribute("price", price);
        model.addAttribute("testPrice", price);

        System.out.println("Time: " + timeOfRide);
        System.out.println("price: " + price);

        //make method find nearest car (from available cars) and assign car to ride and make it non available
        Car foundCar = carService.findNearestCarToLocationAndType(origin, carType1);
        model.addAttribute("foundCarCoords", foundCar.getId()/*getLocation().getAddress()*/);
        //System.out.println(foundCar.toString());
        String timeToPassanger = ridesService.calculateTimeFromDriverToPassanger(origin,foundCar.getLocation().getAddress(), foundCar.getCarType());
        //TODO add time
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyy", Locale.ENGLISH);

        ridesService.save(Ride.builder()
                .startPoint(locationMapper.fromAddressToLocation(origin))
        .destination(locationMapper.fromAddressToLocation(destination))
        .price(price)
        .user(user)
        .car(foundCar)
        .rideDate(formatter.parse(date))
                //provide user ability to choose date and time
        .rideTime(Time.valueOf(LocalTime.now()))
        .status(RideStatus.NEW_RIDE)
        .build());

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
