package com.linukaratnayake.httpclient;

public class PoolingConnectionHttpClientFactory implements HttpClientFactory {
    @Override
    public HttpClientImpl createHttpClient(boolean isNew, boolean isShared) {
        try {
            return new PoolingConnectionHttpClientImpl(isNew, isShared);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
