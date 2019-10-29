package com.taximaps.server.controller;

import com.taximaps.server.service.CarService;
import com.taximaps.server.service.RidesService;
import com.taximaps.server.service.UserService;
import com.taximaps.server.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@RunWith(SpringRunner.class)
//@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MainController mainController;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void contextLoads() {
        assertNotNull(mainController);
    }

    @Test
    public void notLoggedUserShouldSeeLoginPage() throws Exception {
        mockMvc.perform(get("/map").contentType("text/html")).andExpect(status().is(302));
    }

    @Test
    public void LoggedUserShouldSeeMapPage() throws Exception {
        mockMvc.perform(get("/map").contentType("text/html")).andExpect(status().isOk());
    }

    @Test
    public void testGetRidesList() {

    }

    @Test
    public void testGetRide() {

    }

    @Test
    public void testProfile() {

    }

    @Test
    public void testGetOriginAndDestFromUser() {

    }

    @Test
    public void testGetDist() {

    }

    @Test
    public void testGetDirect() {

    }
}