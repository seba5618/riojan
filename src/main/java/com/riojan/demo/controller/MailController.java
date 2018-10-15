package com.riojan.demo.controller;

import com.riojan.demo.model.MailModel;

import com.riojan.demo.utils.FilterOutEmailsByDomains;
import com.riojan.demo.utils.SendgridKey;
import com.sendgrid.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping
public class MailController {

    @Value("${mailFromEmail}")
    private String fromEmail;

    @Value("${mailFromName}")
    private String fromName;

    @Autowired
    private SendgridKey kay;

    @Autowired
    private FilterOutEmailsByDomains filter;


    private static Logger logger = Logger.getLogger(MailController.class);


    @RequestMapping(path="/email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response sendEmail(@RequestBody MailModel emailJson) throws Exception{


        SendGrid sendgrid = new SendGrid(kay.getKey());

        Email from = new Email(this.fromEmail, this.fromName);
        logger.debug("frpom: "+from);

        Content content = new Content("text/plain", emailJson.getBody());
        String subject = emailJson.getSubject();
        Personalization personalization = new Personalization();
        logger.info("To:");


        for(Email aTo: filter.filter(emailJson.getTo())){
            personalization.addTo(aTo);
            logger.info(aTo);
        }

        for(Email aCc: emailJson.getCc()){
            personalization.addTo(aCc);
        }
        for(Email aBcc: emailJson.getBbc()){
            personalization.addTo(aBcc);
        }

        Mail mail = new Mail();
        mail.setSubject(subject);
        mail.addContent(content);
        mail.setFrom(from);

        logger.info("Subject: " + subject);
        logger.info("From: " + from);
        logger.info("Content: ");
        logger.info(content.getValue());

        mail.addPersonalization(personalization);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendgrid.api(request);
            logger.info(response.getStatusCode());
            return response;
        } catch (IOException ex) {
            throw ex;
        }

    }

}
