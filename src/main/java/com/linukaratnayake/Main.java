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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Main {
    public static void main(String[] args) {
        String fromCur = "USD";
        String toCur = "LKR";
        double amountUsd = 1.0;

        String result = get("https://www.x-rates.com/calculator/", fromCur, toCur, amountUsd);

        // Parse the HTML string
        Document doc = Jsoup.parse(result);

        // Select the div with class "ccOutputRslt"
        Element div = doc.selectFirst("div.ccOutputBx");

        // Extract the text content from the div
        if (div != null) {
            System.out.println(div.text());
        } else {
            System.out.println("Value not found!");
        }
    }

    public static String get(String url, String fromCur, String toCur, double amount) {
        String result = null;

        List<NameValuePair> params = new ArrayList<>();
        // GET Query Parameters
        params.add(new BasicNameValuePair("from", fromCur));
        params.add(new BasicNameValuePair("to", toCur));
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