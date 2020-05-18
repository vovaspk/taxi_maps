package com.taximaps.server.controller.web;

import com.taximaps.server.utils.pages.PagesConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DriverWebController {

    @GetMapping(value = {"/drivers"}, produces = "text/html")
    public String map(Model model) {
        return PagesConstants.DRIVER_HOME_PAGE;
    }
}
