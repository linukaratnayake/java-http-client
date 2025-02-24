package com.linukaratnayake.httpclient;

public class BasicConnectionHttpClientFactory implements HttpClientFactory {
    @Override
    public HttpClient createHttpClient(boolean isNew, boolean isShared) {
        try {
            return new BasicConnectionHttpClient(isNew, isShared);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
