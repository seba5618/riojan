package com.riojan.demo.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class MailControllerTest {

    private String URL = "/email";

    @Value("requestBodyTest.json")
    Resource jsonFile;

    @Autowired
    private MailController mailController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.mailController).build();
    }

    @Test
    public void shouldReturnHttpCode400OnPostWithoutParameter() throws Exception {
        mockMvc.perform(post(URL)).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldSendEmailSuccessfully() throws Exception {

        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonFile.getDescription()))
                .andExpect(status().isOk());
    }
}