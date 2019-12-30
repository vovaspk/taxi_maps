package com.taximaps.server.controller.web;

import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.UserProfileFormDto;
import com.taximaps.server.service.UserService;
import com.taximaps.server.utils.pages.PagesConstants;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
public class UserProfileWebController {

    private UserService userService;

    @GetMapping("/user/profile")
    public String profile(Model model, HttpServletRequest req) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserProfileFormDto formDto = new UserProfileFormDto();
        String username = ((UserDetails) principal).getUsername();

        System.out.println("current user: " + username);
        User user = getUser(req);
        model.addAttribute("formDto", formDto);
        model.addAttribute("name", user.getUserName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("password", user.getPassword());
        return PagesConstants.PROFILE_PAGE;
    }

    private User getUser(HttpServletRequest req) {
        String userName = req.getUserPrincipal().getName();
        return (User) userService.loadUserByUsername(userName);
    }
}