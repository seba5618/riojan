package com.riojan.demo.controller;

import com.riojan.demo.model.MailModel;

import com.riojan.demo.model.Quote;
import com.riojan.demo.restClients.QuoteConsumer;
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
import java.util.Collection;


@RestController
@RequestMapping
public class MailController {

    private static final Logger LOGGER = Logger.getLogger(MailController.class);

    @Value("${mailFromEmail}")
    private String fromEmail;
    @Value("${mailFromName}")
    private String fromName;
    @Value("${removeInvalidDomainEmails:true}")
    private boolean filterEmails;

    @Autowired
    private SendgridKey kay;
    @Autowired
    private FilterOutEmailsByDomains filter;
    @Autowired
    private QuoteConsumer quoteConsumer;


    @RequestMapping(path="/email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response sendEmail(@RequestBody MailModel emailJson) throws Exception{

        SendGrid sendgrid = new SendGrid(kay.getKey());

        Email from = new Email(this.fromEmail, this.fromName);
        LOGGER.debug("frpom: "+from);

        Content content = new Content("text/html", emailJson.getBody());
        if(emailJson.isSendQuote()) {
            content.setValue(emailJson.getBody() + quoteConsumer.getQuote().orElse(Quote.EMPTY_QUOTE));
        }

        String subject = emailJson.getSubject();
        Personalization personalization = createPersonalization(emailJson);

        Mail mail = new Mail();
        mail.setSubject(subject);
        mail.addContent(content);
        mail.setFrom(from);

        LOGGER.info("Subject: " + subject);
        LOGGER.info("From: " + from);
        LOGGER.info("Content: ");
        LOGGER.info(content.getValue());

        mail.addPersonalization(personalization);

        return sendMail(sendgrid, mail);
    }

    private Response sendMail(SendGrid sendgrid, Mail mail) throws IOException {
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendgrid.api(request);
            response.getHeaders().put("Filtering external domains emails",String.valueOf(this.filterEmails));
            LOGGER.info(response.getStatusCode());
            return response;
        } catch (IOException ex) {
            LOGGER.error(ex);
            throw ex;
        }
    }

    private Personalization createPersonalization(@RequestBody MailModel emailJson) {

        Collection<Email> filteredTo = emailJson.getTo();
        if(filterEmails){
            filteredTo = filter.filter(emailJson.getTo());
        }

        Collection <Email> filteredCC = emailJson.getCc();
        if(filterEmails){
            filteredCC = filter.filter(emailJson.getCc());
        }

        Collection <Email> filteredBCC = emailJson.getBbc();
        if(filterEmails){
            filteredBCC = filter.filter(emailJson.getBbc());
        }

        Personalization personalization = new Personalization();

        for(Email aTo: filteredTo){
            personalization.addTo(aTo);
            LOGGER.info(aTo);
        }

        for(Email aCc: filteredCC){
            personalization.addTo(aCc);
        }
        for(Email aBcc: filteredBCC){
            personalization.addTo(aBcc);
        }
        return personalization;
    }
}
