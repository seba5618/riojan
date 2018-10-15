package com.riojan.demo.utils;

import com.google.common.collect.Collections2;
import com.riojan.demo.controller.MailController;
import com.sendgrid.Email;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class FilterOutEmailsByDomains {

    private static Logger logger = Logger.getLogger(FilterOutEmailsByDomains.class);
    private String domain;

    @Autowired
    public FilterOutEmailsByDomains(@Value("${validDomain}") String  domain){
        this.domain=domain;
    }

    public Collection <Email> filter (List<Email> origin){
        Collection<Email> filter = Collections2.filter(origin, (email)-> {
            boolean result = false;
            if (email.getEmail().endsWith(domain)) {
                result = true;
            } else {
                logger.info("Filtering out non valid domain email. Valid domain: "+domain +". Email filtered: "+email.getEmail());
            }
            return result;
        });
        return filter;

    }
}
