package com.taximaps.server.controller.web;

import com.taximaps.server.utils.pages.PagesConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ActionsController {


    @GetMapping("/actions")
    public String getActions(Model model){

        return PagesConstants.ACTIONS_PAGE;
    }
}
