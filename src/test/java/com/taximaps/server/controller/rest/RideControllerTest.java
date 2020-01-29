package com.taximaps.server.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taximaps.server.Utils;
import com.taximaps.server.entity.dto.ride.RideFormDto;
import com.taximaps.server.service.RidesService;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DirtiesContext
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RideControllerTest {

    private static final String USER_NAME = "vova";
    private static final Long USER_ID = 1L;
    private static final Long RIDE_ID = 1L;
    private static final String USER_PASSWORD = "1";
    private static final String TEST_ENCRIPTED_PASSWORD = "$2a$08$SEG3LpMLrSmNJ7lDHp9Yo.RqP6XOziBjQhlup74ZrIMxfkS58E7AW";
    private static final String ORIGIN = "origin";
    private static final String DESTINATION = "destination";
    private static final String CAR_TYPE = "ORDINARY";
    private static final String SQL_ADD_TEST_USER = "classpath:" + "sql/user/insertUsers.sql";
    private static final String SQL_ADD_RIDES = "classpath:" + "sql/ride/insert_rides.sql";
    private static final String SQL_CLEAR_CAR_LOCATION_RIDE_TABLES = "classpath:" + "sql/ride/clearLocationCarRideTables.sql";
    private static final String SQL_CLEAR_USER = "classpath:" + "sql/user/clear_users.sql";
    private static final String VIEW_ALL_RIDES_URL = "/rides/all";
    private static final String CREATE_RIDES_URL = "/rides";
    private static final String GET_RIDE_URL = "/rides/1";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @LocalServerPort
    private int port;

    @MockBean
    private RidesService ridesService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void beforeEach() {
        RestAssured.port = port;
    }


    @Sql(value = SQL_ADD_TEST_USER, executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = SQL_CLEAR_USER, executionPhase = AFTER_TEST_METHOD)
    @Test
    public void testCreateRide() throws Exception {
        RideFormDto rideFormDto = getRideFormDto();
        MvcResult mvcResult =  mockMvc.perform(post(CREATE_RIDES_URL)
                .session(Utils.getUserSession(mockMvc, USER_NAME, USER_PASSWORD))
                .content(OBJECT_MAPPER.writeValueAsString(rideFormDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        verify(ridesService,times(1)).bookRide(rideFormDto,USER_NAME);
    }


    @Sql(scripts ={SQL_ADD_TEST_USER,SQL_ADD_RIDES,}, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = {SQL_CLEAR_USER, SQL_CLEAR_CAR_LOCATION_RIDE_TABLES}, executionPhase = AFTER_TEST_METHOD)
    @Test
    public void testGetRidesList() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get(VIEW_ALL_RIDES_URL)
                .session(Utils.getUserSession(mockMvc, USER_NAME, USER_PASSWORD)))
                .andExpect(status().is(200))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        verify(ridesService, times(1)).findUserRides(USER_NAME);
    }

    @Sql(scripts = {SQL_ADD_TEST_USER,SQL_ADD_RIDES}, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = {SQL_CLEAR_USER, SQL_CLEAR_CAR_LOCATION_RIDE_TABLES}, executionPhase = AFTER_TEST_METHOD)
    @Test
    public void testGetRide() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(GET_RIDE_URL)
                .session(Utils.getUserSession(mockMvc, USER_NAME, USER_PASSWORD)))
                .andExpect(status().is(200))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        verify(ridesService, times(1)).findRideById(RIDE_ID);

    }

    private RideFormDto getRideFormDto() {
        RideFormDto rideFormDto = new RideFormDto();
        rideFormDto.setOrigin(ORIGIN);
        rideFormDto.setDestination(DESTINATION);
        rideFormDto.setDate(new Date());
        rideFormDto.setCarType(CAR_TYPE);

        return rideFormDto;
    }
}