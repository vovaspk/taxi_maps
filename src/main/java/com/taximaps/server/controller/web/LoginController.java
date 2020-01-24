package com.taximaps.server.controller.web;

import com.taximaps.server.utils.pages.PagesConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model){
        return PagesConstants.LOGIN_PAGE;
    }
}
