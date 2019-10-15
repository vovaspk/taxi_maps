package com.taximaps.server.controller;

import com.taximaps.server.domain.User;
import com.taximaps.server.service.UserService;
import com.taximaps.server.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller()
public class RegistrationController {

    private UserService userService;

    @Autowired
    public RegistrationController(UserServiceImpl userServiceImpl) {
        this.userService = userServiceImpl;
    }


    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User user, Model model) {
        if (user.getPassword() != null ) {
            model.addAttribute("passwordError", "Passwords are not the same");
        }
        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }

}
