package com.taximaps.server.controller;

import com.taximaps.server.dao.UserDao;
import com.taximaps.server.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private UserDao userDao;

    @PostMapping("/login")
    public String login(User user, Model model){
        User user1 = userDao.findByUserName(user.getUserName());
        if(user1 != null){
            return "main";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model){
        return "main";
    }
}
