package com.riojan.demo.restClients;

import com.riojan.demo.model.Quote;

import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

@Component
public class QuoteConsumer {

    private final String URL = "https://talaikis.com/api/quotes/random/";

    public Quote getQuote(){

        try{
            Client clientBuilder = ClientBuilder.newClient();
            Quote quote  = clientBuilder.target(URL).request(MediaType.APPLICATION_JSON_TYPE).get(Quote.class);
            System.out.println(quote.getQuote());
            return quote;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }


}
