package com.riojan.demo.controller;

import com.riojan.demo.model.MailModel;
import com.sendgrid.;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
public class Mail {

    @RequestMapping(path="/email", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String sendEmail(@RequestBody MailModel mail) throws Exception{
        SendGrid sendgrid = new SendGrid("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");

        Request request = new Request();
        request.setMethod(Method.GET);
        request.setEndpoint("mail/send");
        Response response = sendgrid.api(request);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());
        return mail.toString();
    }

}
