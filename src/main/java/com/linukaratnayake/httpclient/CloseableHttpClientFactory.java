package com.linukaratnayake.httpclient;

import org.apache.hc.client5.http.io.HttpClientConnectionManager;

public interface CloseableHttpClientFactory {

    public static HttpClientConnectionManager getConnectionManager() {
        return null;
    }
}
