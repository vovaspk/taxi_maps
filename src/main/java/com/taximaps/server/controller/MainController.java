package com.taximaps.server.controller;

import com.google.maps.errors.ApiException;
import com.taximaps.server.maps.JsonReader;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import static com.taximaps.server.maps.JsonReader.getDirectionResult;
import static com.taximaps.server.maps.JsonReader.getDriveDistanceAndTime;

@Controller
public class MainController {

    @GetMapping(value = {"/map", "/main"}, produces = "text/html")
    public String map (Model model){
        return "main";
    }

    @PostMapping(value = "/processInput", produces = "text/html")
    public String getOriginAndDestFromUser(@RequestParam String origin, @RequestParam String destination, Model model) throws IOException, ApiException, InterruptedException {
        String response = JsonReader.sendRequest(origin, destination);
        // atach response to model ?
        model.addAttribute("origin", origin);
        model.addAttribute("destination", destination);
        model.addAttribute("response", response);
        //draw route on map in html
        //return the html page with map and route on it
        return "responsePage";

    }

    @GetMapping("/getDist")
    public String getDist(String startAddress, String endAddress, Model model) throws InterruptedException, ApiException, IOException {
        model.addAttribute("dist",getDriveDistanceAndTime(startAddress,endAddress));
        return "responsePage";
    }

    @GetMapping("/getDirect")
    public String getDirect(String startAddress, String endAddress, Model model) throws InterruptedException, ApiException, IOException {
        String directionJson = getDirectionResult(startAddress,endAddress);
        model.addAttribute("start", startAddress);
        model.addAttribute("end", endAddress);
        model.addAttribute("direct", directionJson);
        return "responsePage";
    }





}
