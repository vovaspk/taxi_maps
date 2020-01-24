package com.taximaps.server.controller.web;

import com.taximaps.server.utils.pages.PagesConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class MainController {

    @GetMapping(value = {"/", "/map", "/main"}, produces = "text/html")
    public String map(Model model) {
        return PagesConstants.MAIN_PAGE;
    }

    @GetMapping("/processInput")
    public String getProcessInputPage(Model model){

        return PagesConstants.RESPONSE_PAGE;
    }

}
