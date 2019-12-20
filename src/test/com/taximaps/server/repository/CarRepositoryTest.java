package com.taximaps.server.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findAll() {
    }

    @Test
    public void findCarsByCarStatus() {
    }

    @Test
    public void findCarsByLocation() {
    }

    @Test
    public void getAllCarsInfo(){
        System.out.println(carRepository.getAllCarsInfo().get(0).toString());
    }
}