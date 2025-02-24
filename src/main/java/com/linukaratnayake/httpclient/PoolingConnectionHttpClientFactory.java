package com.linukaratnayake.httpclient;

public class PoolingConnectionHttpClientFactory implements HttpClientFactory {
    @Override
    public HttpClient createHttpClient(boolean isNew, boolean isShared) {
        try {
            return new PoolingConnectionHttpClient(isNew, isShared);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
