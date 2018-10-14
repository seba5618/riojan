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
        SendGrid sendgrid = new SendGrid("");

        Email from = new Email("test@example.com");
        String subject = "Sending with SendGrid is Fun";
        Email to = new Email("sebaze@gmail.com");
        Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
        return mail.toString();
    }

}
