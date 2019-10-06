package com.taximaps.server.controller;

import com.taximaps.server.maps.JsonReader;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class MainController {

    @GetMapping(value = "/map", produces = "text/html")
    public String map (Model model){
        return "main";
    }

    @PostMapping(value = "/processInput", produces = "text/html")
    public String getOriginAndDestFromUser(@RequestParam String origin, @RequestParam String destination, Model model) throws IOException {
        String response = JsonReader.sendRequest(origin, destination);
        // atach response to model ?
        model.addAttribute("origin", origin);
        model.addAttribute("destination", destination);
        model.addAttribute("response", response);
        //draw route on map in html
        //return the html page with map
        return "responsePage";

    }





}
