package com.taximaps.server.controller.web;

import com.taximaps.server.entity.User;
import com.taximaps.server.service.UserService;
import com.taximaps.server.service.impl.UserServiceImpl;
import com.taximaps.server.utils.pages.PagesConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

//    private UserService userService;
//    @Autowired
//    public LoginController(UserServiceImpl userService) {
//        this.userService = userService;
//    }

//    @PostMapping("/login")
//    public String login(@RequestParam String userName, Model model){
//        User foundUser = (User) userService.loadUserByUsername(userName);
//        if(foundUser != null){
//            model.addAttribute("user", userName);
//            return PagesConstants.MAIN_PAGE;
//        }
//        return "redirect:/login";
//    }

    @GetMapping("/login")
    public String login(Model model){
        return PagesConstants.LOGIN_PAGE;
    }
}
