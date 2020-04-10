package com.taximaps.server.controller.web;

import com.taximaps.server.entity.Role;
import com.taximaps.server.entity.User;
import com.taximaps.server.service.UserService;
import com.taximaps.server.service.impl.UserServiceImpl;
import com.taximaps.server.utils.pages.PagesConstants;
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
        model.addAttribute("role_user", Role.USER);
        model.addAttribute("role_driver", Role.DRIVER);
        return PagesConstants.REGISTRATION_PAGE;
    }

    @PostMapping("/registration")//user is null
    public String addUser(@Valid @ModelAttribute User user, Model model) {
        model.addAttribute("user", user);
        if (user.getPassword() != null ) {
            model.addAttribute("passwordError", "Passwords are not the same");
        }
        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return PagesConstants.REGISTRATION_PAGE;
        }

        return "redirect:/login";
    }

}
//TODO create plan what app should be and how to reproduce it step by step by step
// 1. ssl certificate for https,
// 2. ssl for driver module and for geolocation of users
// 3. test endpoint to view path
// 4. algorithm [0], [1], [2] choose different paths, result[1] check difference
// 5. algorithm a* run and add  koefs, how much snow road koef, death rate koef, bad road koef, traffic(ready)
// 6.for geolocation of users
// 7. if have time saved places
// navbar for drivers should be different from usersbar, links are wrong