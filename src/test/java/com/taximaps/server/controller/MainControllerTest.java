package com.taximaps.server.controller;

import com.taximaps.server.controller.web.MainController;
import com.taximaps.server.entity.User;
import com.taximaps.server.mapper.LocationMapper;
import com.taximaps.server.repository.CarRepository;
import com.taximaps.server.repository.RidesRepository;
import com.taximaps.server.repository.UserRepository;
import com.taximaps.server.service.impl.CarServiceImpl;
import com.taximaps.server.service.impl.RidesServiceImpl;
import com.taximaps.server.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {
    //1111
    private static final String user_password_hashed = "$2a$08$zR2XQakN5rDX4RCFoy8c/ec90VxKrGjHJ4cIoND5ceBhqEmtqIuKy";
    private static final String user_password = "1111";
    private static final String user_name = "vova";

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private CarServiceImpl carService;

    @MockBean
    private RidesServiceImpl ridesService;

    @MockBean
    private RidesRepository ridesRepository;

    @MockBean
    private CarRepository carRepository;

    @MockBean
    private LocationMapper locationMapper;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MainController mainController;


    @Autowired
    WebApplicationContext wac;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();

        initMocks(this);
    }

    @Test
    public void contextLoads() {
        assertNotNull(mainController);
    }

    @Test
    public void notLoggedUserShouldSeeLoginPage() throws Exception {
        mockMvc
                .perform(get("/map")
                        .contentType("text/html"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("http://localhost/login"));
    }


    @Test(timeout = 20000)
    @WithMockUser(username = "vova", password = "1111")
    public void LoggedUserShouldSeeMapPage() throws Exception {
        User user = new User();
        user.setUserName(user_name);
        user.setPassword(user_password_hashed);
        user.setEmail("vova");
        user.setActive(true);
        Mockito.when(userService.loadUserByUsername(user_name)).thenReturn(user);
        User testUser = (User) userService.loadUserByUsername(user_name);

        mockMvc.perform(formLogin().user(user_name).password("1111"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
        .andExpect(authenticated().withUsername(user_name));
        //mockMvc.perform(get("/map").contentType("text/html")).andExpect(status().isOk());
    }

    @Test
    public void testGetRidesList() {

    }

    @Test
    public void testGetRide() {

    }

    @Test
    @WithMockUser(username = "vova", password = "1111")
    public void testProfile() throws Exception {
        User user = new User();
        user.setUserName(user_name);
        user.setPassword(user_password_hashed);
        user.setEmail("vova");
        user.setActive(true);
        Mockito.when(userService.loadUserByUsername(user_name)).thenReturn(user);
        User testUser = (User) userService.loadUserByUsername(user_name);

        mockMvc.perform(formLogin().user(user_name).password("1111"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername(user_name));



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