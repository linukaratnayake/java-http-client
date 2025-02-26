package com.linukaratnayake.httpclient;

public interface HttpClient {
    <T> T get(String url);

//    <T> T post(String url, String body);

//    void close();
//
//    void closeConnectionManager();
}
