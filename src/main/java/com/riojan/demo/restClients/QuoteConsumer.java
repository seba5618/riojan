package com.riojan.demo.restClients;

import com.riojan.demo.model.Quote;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Component
public class QuoteConsumer {

    @Value("${quoteRestService:https://talaikis.com/api/quotes/random/}")
    private String URL;

    private static final Logger LOGGER = Logger.getLogger(QuoteConsumer.class);

    public Optional<Quote> getQuote(){
        Quote quote = null;
        try{
            Client clientBuilder = ClientBuilder.newClient();
            quote  = clientBuilder.target(URL).request(MediaType.APPLICATION_JSON_TYPE).get(Quote.class);

        }catch (Exception e){
            LOGGER.error(e);
        }
        return Optional.ofNullable(quote);

    }


}
