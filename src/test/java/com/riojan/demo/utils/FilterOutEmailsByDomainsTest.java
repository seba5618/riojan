package com.riojan.demo.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendgrid.Email;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FilterOutEmailsByDomainsTest {

    private List<Email> emails = new ArrayList<Email>();

    @Autowired
    private FilterOutEmailsByDomains filter;

    @Value("classpath:emailsMock.json")
    Resource resourceFile;

    @Value("${validDomain}") 
    String  domain;

    @Before
    public void setUp() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference typeReference = new TypeReference<List<Email>>(){};
        InputStream inputStream = resourceFile.getInputStream();
        emails = mapper.readValue(inputStream, typeReference);
    }

    @Test
    public void testFilter() throws Exception {

        Collection<Email> filtered = this.filter.filter(emails);
        for(Email email : filtered) {
            Assert.assertTrue(email.getEmail().endsWith(domain));
        }
    }
}