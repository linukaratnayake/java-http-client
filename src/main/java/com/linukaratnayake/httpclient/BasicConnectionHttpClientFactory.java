package com.linukaratnayake.httpclient;

public class BasicConnectionHttpClientFactory implements HttpClientFactory {
    @Override
    public HttpClientImpl createHttpClient(boolean isNew, boolean isShared) {
        try {
            return new BasicConnectionHttpClientImpl(isNew, isShared);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
