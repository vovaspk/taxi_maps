package com.taximaps.server;

import com.taximaps.server.config.TestAppConfig;
import com.taximaps.server.controller.MainController;
import com.taximaps.server.domain.Role;
import com.taximaps.server.domain.User;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static com.taximaps.server.Utils.getUserSession;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
@WebAppConfiguration
@WebMvcTest(controllers = MainController.class)
public class LoginLogoutTest {
    private static final String CREATE_USERS_SQL = "classpath:sql/createUsers.sql";
    private static final String CLEAR_DB_SQL = "classpath:sql/clearDb.sql";
    private static final String TEST_USER_EMAIL = "Vova";
    private static final String TEST_USER_PASS = "1";
    private static final String DISABLED_TEST_USER_EMAIL = "John.P@gmail.com";
    private static final String WRONG_TEST_USER_EMAIL = "2";
    private static final String DISABLED_TEST_USER_PASS = "Be77erAnd55Stronger";
    private static final String LOGIN_ERROR_URL = "/login?error";
    private static final String UserName = "UserVova";
    //1234
    public static final String PASSWORD = "$2a$08$MOl8kaL0fc6u1fgAQeR0FePqfybWXtpl2XO9j7gNH/LcvNw9dmBk2";

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
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MainController mainController;


    @Autowired
    WebApplicationContext wac;

    @Before
    public void init() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();

        initMocks(this);
    }

    @Test(timeout = 20000)
    public void loginTest() throws Exception {
        User user = new User();
        user.setUserName(UserName);
        user.setPassword(PASSWORD);
        user.setEmail("vova");
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        Mockito.when(userService.loadUserByUsername(UserName)).thenReturn(user);
        User testUser = (User) userService.loadUserByUsername(UserName);
        mockMvc.perform(formLogin().user(UserName).password("1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername(UserName));
    }

    @Test(timeout = 20000)
    public void loginWithWrongPassTest() throws Exception {
        mockMvc.perform(formLogin().user(TEST_USER_EMAIL).password("wrong"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LOGIN_ERROR_URL))
                .andExpect(unauthenticated());
    }

    @Test(timeout = 20000)
    public void loginWithDisabledUserTest() throws Exception {
        mockMvc.perform(formLogin().user(DISABLED_TEST_USER_EMAIL).password(DISABLED_TEST_USER_PASS))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LOGIN_ERROR_URL))
                .andExpect(unauthenticated());
    }

    @Test(timeout = 20000)
    public void loginWithWrongUserTest() throws Exception {
        mockMvc.perform(formLogin().user(WRONG_TEST_USER_EMAIL).password(DISABLED_TEST_USER_PASS))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LOGIN_ERROR_URL))
                .andExpect(unauthenticated());
    }


    @Test(timeout = 1000)
    public void logoutTest() throws Exception {
        mockMvc.perform(post("/logout")
                .session(getUserSession(mockMvc, TEST_USER_EMAIL, TEST_USER_PASS)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"))
                .andExpect(unauthenticated());
    }
}
