package com.riojan.demo.restClients;

import com.riojan.demo.model.Quote;

import com.sun.org.apache.xpath.internal.operations.Quo;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
public class QuoteConsumer {

    private final String URL = "https://talaikis.com/api/quotes/random/";

    public Quote getQuote(){

        try{
            Client clientBuilder = ClientBuilder.newClient();
            WebTarget target = clientBuilder.target(URL);
            Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
            System.out.println("ASDFASDFASDF"+response);
            Quote quote = target.request().get(Quote.class);
            System.out.println(quote.getQuote());
            return quote;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public static void main(String[] args) {
        new QuoteConsumer().getQuote();
    }
}
