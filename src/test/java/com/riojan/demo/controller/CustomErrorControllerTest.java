package com.riojan.demo.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CustomErrorControllerTest {

    private String FAKE_URL = "/fakeurl";

    @Autowired
    private CustomErrorController customErrorController;

    private MockMvc mockMvc;

    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.customErrorController).build();
    }


    @Test
    public void invalidURL() throws Exception {
        mockMvc.perform(get(FAKE_URL))
                .andExpect(status().is(404))
                .andReturn();
    }
}