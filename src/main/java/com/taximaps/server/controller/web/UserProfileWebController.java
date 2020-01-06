package com.taximaps.server.controller.web;

import com.taximaps.server.service.UserService;
import com.taximaps.server.utils.pages.PagesConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
public class UserProfileWebController {

    private UserService userService;

    @GetMapping("/profile")
    public String profile(Model model, HttpServletRequest req) {
        return PagesConstants.PROFILE_PAGE;
    }

}