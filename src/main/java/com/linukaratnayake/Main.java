//package com.linukaratnayake;
//
//import com.linukaratnayake.httpclient.Client;
//
//public class Main {
//    public static void main(String[] args) {
/// /        String fromCur = "USD";
/// /        String toCur = "LKR";
/// /        double amountUsd = 1.0;
/// /
/// /        List<NameValuePair> params = new ArrayList<>();
/// /        // GET Query Parameters
/// /        params.add(new BasicNameValuePair("from", fromCur));
/// /        params.add(new BasicNameValuePair("to", toCur));
/// /        params.add(new BasicNameValuePair("amount", String.valueOf(amountUsd)));
//
////        String result = Client.get("https://www.x-rates.com/calculator/", params).toString();
//
//        System.setProperty("jdk.tls.namedGroups", "X25519MLKEM768,x25519,secp256r1,secp384r1,secp521r1");
//
////        String result = Client.get("https://localhost:9443/").toString();
//        String result = Client.get("https://google.com/").toString();
//
//        System.out.println(result);
//
////        // Parse the HTML string
////        Document doc = Jsoup.parse(result);
////
////        // Select the div with class "ccOutputRslt"
////        Element div = doc.selectFirst("div.ccOutputBx");
////
////        // Extract the text content from the div
////        if (div != null) {
////            System.out.println(div.text());
////        } else {
////            System.out.println("Value not found!");
////        }
//    }
//}