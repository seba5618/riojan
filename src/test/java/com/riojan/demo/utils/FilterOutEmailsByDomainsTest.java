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
        ArrayList<Email> emails = new ArrayList<>();

        Email email = new Email();
        email.setName("Max");
        email.setEmail("maxi.ruiz140@gmail.com");
        emails.add(email);

        email = new Email();
        email.setName("Tere");
        email.setEmail("teresita.garro@gmail.com");
        emails.add(email);

        email = new Email();
        email.setName("John");
        email.setEmail("alchichon.234@gmail.com");
        emails.add(email);

        email = new Email();
        email.setName("TJuanere");
        email.setEmail("tjiuanere@rakenapp.com");
        emails.add(email);

        email = new Email();
        email.setName("Emilia");
        email.setEmail("emilia@hotmail.com");
        emails.add(email);

        email = new Email();
        email.setName("TePepere");
        email.setEmail("te.pep@outlook.com");
        emails.add(email);

        email = new Email();
        email.setName("Fulano");
        email.setEmail("fulano@rakenapp.com");
        emails.add(email);

        email = new Email();
        email.setName("Mengano");
        email.setEmail("meng123@gmarakenappil.com");
        emails.add(email);

        return emails;
    }

   }