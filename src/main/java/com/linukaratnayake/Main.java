package com.linukaratnayake;

import com.linukaratnayake.httpclient.Client;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fromCur = "USD";
        String toCur = "LKR";
        double amountUsd = 1.0;

        List<NameValuePair> params = new ArrayList<>();
        // GET Query Parameters
        params.add(new BasicNameValuePair("from", fromCur));
        params.add(new BasicNameValuePair("to", toCur));
        params.add(new BasicNameValuePair("amount", String.valueOf(amountUsd)));

        String result = Client.get("https://www.x-rates.com/calculator/", params).toString();

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
}