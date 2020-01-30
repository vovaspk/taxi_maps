package com.taximaps.server.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taximaps.server.Utils;
import com.taximaps.server.entity.CarType;
import com.taximaps.server.entity.dto.car.FindCarDto;
import com.taximaps.server.entity.status.CarStatus;
import com.taximaps.server.service.CarService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CarControllerTest {

    private static final String SQL_ADD_TEST_USER = "classpath:" + "sql/user/insertUsers.sql";
    private static final String SQL_CLEAR_USER = "classpath:" + "sql/user/clear_users.sql";
    private static final String USER_NAME = "vova";
    private static final String USER_PASSWORD = "1";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String GET_FREE_CARS_URL = "/cars/free";
    private static final String GET_ALL_CARS_URL = "/cars/all";
    private static final String FIND_CAR_URL = "/cars/find";
    private static final String TEST_ADDRESS = "test_address";

    @LocalServerPort
    private int port;

    @MockBean
    private CarService carService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        RestAssured.port = port;
    }

    @Sql(value = SQL_ADD_TEST_USER, executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = SQL_CLEAR_USER, executionPhase = AFTER_TEST_METHOD)
    @Test
    void getFreeCars() throws Exception {

        MvcResult mvcResult =  mockMvc.perform(get(GET_FREE_CARS_URL)
                .session(Utils.getUserSession(mockMvc, USER_NAME, USER_PASSWORD))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        verify(carService,times(1)).findCarsByCarStatus(CarStatus.FREE);
    }

    @Sql(value = SQL_ADD_TEST_USER, executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = SQL_CLEAR_USER, executionPhase = AFTER_TEST_METHOD)
    @Test
    void testFindNearestCarToLocation() throws Exception {
        FindCarDto findCarDto = new FindCarDto();
        findCarDto.setCarType(CarType.ORDINARY);
        findCarDto.setAddress(TEST_ADDRESS);
        MvcResult mvcResult =  mockMvc.perform(get(FIND_CAR_URL)
                .session(Utils.getUserSession(mockMvc, USER_NAME, USER_PASSWORD))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(findCarDto)))
                .andExpect(status().is(200))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        verify(carService,times(1)).findNearestCarToLocationAndType(TEST_ADDRESS, CarType.ORDINARY);
    }

    @Sql(value = SQL_ADD_TEST_USER, executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = SQL_CLEAR_USER, executionPhase = AFTER_TEST_METHOD)
    @Test
    void getAllCars() throws Exception {
        MvcResult mvcResult =  mockMvc.perform(get(GET_ALL_CARS_URL)
                .session(Utils.getUserSession(mockMvc, USER_NAME, USER_PASSWORD))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        verify(carService,times(1)).findAll();
    }
}