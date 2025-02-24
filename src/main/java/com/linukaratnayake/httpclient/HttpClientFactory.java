package com.linukaratnayake.httpclient;

public interface HttpClientFactory {
    public HttpClient createHttpClient(boolean isNew, boolean isShared);
}
