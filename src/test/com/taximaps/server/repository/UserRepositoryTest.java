package com.taximaps.server.repository;

import com.taximaps.server.controller.MainController;
import com.taximaps.server.domain.Role;
import com.taximaps.server.domain.User;
import com.taximaps.server.service.UserService;
import com.taximaps.server.service.impl.UserServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    private static final String CREATE_USERS_SQL = "classpath:sql/createUsers.sql";
    private static final String CLEAR_DB_SQL = "classpath:sql/clearDb.sql";
    private static final String TEST_USER_EMAIL = "Vova";
    private static final String TEST_USER_PASS = "1";
    private static final String DISABLED_TEST_USER_EMAIL = "John.P@gmail.com";
    private static final String WRONG_TEST_USER_EMAIL = "2";
    private static final String DISABLED_TEST_USER_PASS = "Be77erAnd55Stronger";
    private static final String LOGIN_ERROR_URL = "/login?error";


    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserServiceImpl userService;


    @After
    public void tearDown() throws Exception {
    }

//    @Sql(value = CREATE_USERS_SQL, executionPhase = BEFORE_TEST_METHOD)
//    @Sql(value = CLEAR_DB_SQL, executionPhase = AFTER_TEST_METHOD)
    @Test
    public void findByUserName() {
        String userName = "UserVova";
        User user = new User();
        user.setId((long) 3);
        user.setUserName(userName);
        user.setEmail("testUser@gmail.com");
        user.setPassword("1234");
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        User user1 = userRepository.findByUserName("UserVova");
        assertNotNull(user1);
    }

    @Test
    public void saveUser(){
        String userName = "testUser";
        User user = new User();
        user.setId((long) 3);
        user.setUserName(userName);
        user.setEmail("testUser@gmail.com");
        user.setPassword("1234");
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        User userFound = userRepository.findByUserName(userName);
        assertNotNull(userFound);
    }
}