package com.riojan.demo.controller;

import com.riojan.demo.model.MailModel;

import com.sendgrid.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping
public class MailController {

    @RequestMapping(path="/email", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String sendEmail(@RequestBody MailModel emailJson) throws Exception{
        SendGrid sendgrid = new SendGrid("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");

        Email from = new Email("riojan@cambiame.com");

        Content content = new Content("text/plain", emailJson.getBody());
        String subject = emailJson.getSubject();
        Personalization personalization = new Personalization();
        for(String aTo: emailJson.getTo()){
            personalization.addTo(new Email(aTo));
        }

        for(String aCc: emailJson.getCc()){
            personalization.addTo(new Email(aCc));
        }
        for(String aBcc: emailJson.getBbc()){
            personalization.addTo(new Email(aBcc));
        }

        Mail mail = new Mail();
        mail.setSubject(subject);
        mail.addContent(content);
        mail.setFrom(from);

        mail.addPersonalization(personalization);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendgrid.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
        return mail.toString();
    }

}
