package com.linukaratnayake.httpclient;

public interface HttpClientFactory {
    public HttpClientImpl createHttpClient(boolean isNew, boolean isShared);
}
