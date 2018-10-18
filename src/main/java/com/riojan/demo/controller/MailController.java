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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;


@RestController
@RequestMapping
public class MailController {



    @Configuration
    static class SendGridConfiguration {

        @Autowired
        protected SendgridKey kay;

        @Bean
        public SendGrid sendGrid() throws Exception {
            return new SendGrid(kay.getKey());
        }

    }

    private static final Logger LOGGER = Logger.getLogger(MailController.class);

    @Value("${mailFromEmail}")
    private String fromEmail;
    @Value("${mailFromName}")
    private String fromName;
    @Value("${removeInvalidDomainEmails:true}")
    private boolean filterEmails;

    @Autowired
    private SendGrid sendGrid;
    @Autowired
    private FilterOutEmailsByDomains filter;
    @Autowired
    private QuoteConsumer quoteConsumer;




    @RequestMapping(path="/email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response sendEmail(@RequestBody MailModel emailJson) throws Exception{



        Email from = new Email(this.fromEmail, this.fromName);
        LOGGER.debug("from: "+ from.getName() + " (" + from.getEmail() + ")");

        Content content = new Content("text/html", emailJson.getBody());
        if(emailJson.isEnrich()) {
            Quote quote = quoteConsumer.getQuote().orElse(Quote.EMPTY_QUOTE);
            StringBuilder builder = new StringBuilder(emailJson.getBody());
            builder.append("\n-----------Quote--------\n")
                    .append(" Author : ").append(quote.getAuthor())
                    .append(" Category : ").append(quote.getCat())
                    .append("\n").append(quote.getQuote());
            content.setValue(builder.toString());
        }

        String subject = emailJson.getSubject();
        Personalization personalization = createPersonalization(emailJson);

        Mail mail = new Mail();
        mail.setSubject(subject);
        mail.addContent(content);
        mail.setFrom(from);

        LOGGER.debug(emailJson);

        mail.addPersonalization(personalization);

        return sendMail(mail);
    }

    private Response sendMail(Mail mail) throws IOException {
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            if(response.getHeaders()==null) {
                response.setHeaders(new HashMap<>());
            }
            response.getHeaders().put("Filtering external domains emails", String.valueOf(this.filterEmails));
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
