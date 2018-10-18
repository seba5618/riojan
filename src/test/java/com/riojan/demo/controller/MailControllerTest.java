package com.riojan.demo.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.riojan.demo.model.MailModel;
import com.riojan.demo.restClients.QuoteConsumer;
import com.riojan.demo.utils.FilterOutEmailsByDomains;
import com.riojan.demo.utils.SendgridKey;
import com.sendgrid.Email;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.ArrayList;

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

        ArrayList<Email> toList = new ArrayList<>();
        MailModel test = new MailModel();

        Email to = new Email();
        to.setEmail("maxi.ruiz140@gmail.com");
        to.setName("Maxi");
        toList.add(to);

        to = new Email();
        to.setEmail("maxi_ruiz140@hotmail.com");
        to.setName("Maximiliano");
        toList.add(to);

        test.setTo(toList);
        test.setSubject("subject");
        test.setBody("body");
        test.setEnrich(false);


        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(test)))

                .andExpect(status().isOk());
        Mockito.verify(quoteConsumer,Mockito.times(0)).getQuote();
    }

    @Test
    public void shouldSendEmailSuccessfullyWithQuote() throws Exception {

        ArrayList<Email> toList = new ArrayList<>();
        MailModel test = new MailModel();

        Email to = new Email();
        to.setEmail("maxi.ruiz140@gmail.com");
        to.setName("Maxi");
        toList.add(to);

        test.setTo(toList);
        test.setSubject("subject");
        test.setBody("body");
        test.setEnrich(true);


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