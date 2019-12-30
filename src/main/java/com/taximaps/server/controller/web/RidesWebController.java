package com.taximaps.server.controller.web;

import com.taximaps.server.utils.pages.PagesConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RidesWebController {

    @GetMapping("/userRides")
    public String getRides(){
        return PagesConstants.RIDES_PAGE;
    }
}
