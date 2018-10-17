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

    private static final String DOMAIN = "gmail.com";

    private FilterOutEmailsByDomains filter = new FilterOutEmailsByDomains(DOMAIN);

    @Test
    public void testFilter() throws Exception {

        Collection<Email> filtered = this.filter.filter(this.listOfEmailsForTest());
        for(Email email : filtered) {
            Assert.assertTrue(email.getEmail().endsWith(DOMAIN));
        }
    }

    private List<Email> listOfEmailsForTest(){
        Email email = new Email();

        email.setName("Max");
        email.setEmail("maxi.ruiz140@gmail.com");

        Email email2 = new Email();
        email2.setName("salchi");
        email2.setEmail("salchichon.234@gmail.com");

        ArrayList<Email> emails = new ArrayList<>();
        emails.add(email);
        emails.add(email2);
        return emails;
    }


}