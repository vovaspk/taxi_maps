package com.taximaps.server.controller;

import com.taximaps.server.domain.User;
import com.taximaps.server.service.UserService;
import com.taximaps.server.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")//user is null
    public String addUser(@Valid @ModelAttribute User user, Model model) {
        model.addAttribute("user", user);
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
