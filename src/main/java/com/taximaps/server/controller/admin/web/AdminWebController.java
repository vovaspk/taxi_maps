package com.taximaps.server.controller.admin.web;

import com.taximaps.server.utils.pages.PagesConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminWebController {

    @GetMapping("/admin")
    public String getAdminMainPage(){
        return PagesConstants.ADMIN_HOME_PAGE;
    }

}
