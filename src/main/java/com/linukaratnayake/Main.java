package com.linukaratnayake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        double amountUsd = 1.0;
        String result = post("https://www.x-rates.com/calculator/?from=USD&to=LKR", amountUsd);
        System.out.println(result);
    }

    public static String post(String url, double amount) {
        String result = null;

        List<NameValuePair> params = new ArrayList<>();
        // GET Query Parameters
        params.add(new BasicNameValuePair("amount", String.valueOf(amount)));

        // Add to the request URL
        try {
            URI uri = new URIBuilder(new URI(url))
                    .addParameters(params)
                    .build();
            try {
                Response response = Request.get(uri.toURL().toString()).execute();
                result = response.returnContent().asString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}