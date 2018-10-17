package com.riojan.demo.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.riojan.demo.model.MailModel;
import com.riojan.demo.restClients.QuoteConsumer;
import com.riojan.demo.utils.FilterOutEmailsByDomains;
import com.riojan.demo.utils.SendgridKey;
import com.sendgrid.Email;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MailControllerTest {

    @Configuration
    static class MailControllerTestConfiguration {

        @Bean
        public QuoteConsumer quoteConsumer() {
            return Mockito.mock(QuoteConsumer.class);
        }

        @Bean
        public SendgridKey sendgridKey() {
            return Mockito.mock(SendgridKey.class);
        }

        @Bean
        public MailController mailController() {
            return new MailController();
        }

        @Bean
        public SendGrid sendGrid() throws Exception {
            SendGrid sendGrid = Mockito.mock(SendGrid.class);
            Response response = new Response();
            Mockito.when(sendGrid.api(ArgumentMatchers.any(Request.class))).thenReturn(response);
            return sendGrid;
        }

        @Bean
        public FilterOutEmailsByDomains filterOutEmailsByDomains() {
            return Mockito.mock(FilterOutEmailsByDomains.class);
        }
    }

    private String URL = "/email";


    @Autowired
    private MailController mailController;

    @Autowired
    private QuoteConsumer quoteConsumer;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.mailController).build();
    }


    @Test
    public void shouldReturnHttpCode400OnPostWithoutParameter() throws Exception {
        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldSendEmailSuccessfully() throws Exception {

        MailModel test = new MailModel();
        Email to = new Email();
        to.setEmail("sebaze@gmail.com");
        to.setName("seba");
        ArrayList<Email> toList = new ArrayList<>();
        toList.add(to);

        test.setTo(toList);
        test.setSubject("subject");
        test.setSendQuote(false);
        test.setBody("body");


        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(test)))

                .andExpect(status().isOk());
        Mockito.verify(quoteConsumer,Mockito.times(0)).getQuote();
    }

    @Test
    public void shouldSendEmailSuccessfullyWithQuote() throws Exception {

        MailModel test = new MailModel();
        Email to = new Email();
        to.setEmail("sebaze@gmail.com");
        to.setName("seba");
        ArrayList<Email> toList = new ArrayList<>();
        toList.add(to);

        test.setTo(toList);
        test.setSubject("subject");
        test.setBody("body");
        test.setSendQuote(true);


        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(test)))

                .andExpect(status().isOk());
        Mockito.verify(quoteConsumer,Mockito.times(1)).getQuote();

    }

    @After
    public void resetMocks(){
        Mockito.reset(quoteConsumer);
    }


    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

}