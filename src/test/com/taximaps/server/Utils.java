package com.taximaps.server;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

public class Utils {
    public static MockHttpSession getUserSession(MockMvc mockMvc, String username, String pass) throws Exception {
        MvcResult mvcResult = mockMvc.perform(formLogin().user(username).password(pass)).andReturn();
        return (MockHttpSession) mvcResult.getRequest().getSession();
    }
}
