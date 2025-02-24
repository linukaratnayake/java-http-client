package com.linukaratnayake.httpclient;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.http.HttpEntity;

import java.io.IOException;
import java.io.InputStream;

public abstract class HttpClient {
//    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private final HttpClientConnectionManager connectionManager;
    private final CloseableHttpClient httpClient;

    protected HttpClient() {
        this.connectionManager = new BasicHttpClientConnectionManager();
        this.httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setConnectionManagerShared(false)  // Default to false
                .build();
    }

    protected HttpClient(HttpClientConnectionManager connectionManager, boolean isShared) {
        this.connectionManager = connectionManager;
        this.httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setConnectionManagerShared(isShared)
                .build();
    }

    public InputStream get(String url) throws IOException {
        HttpGet getUrl = new HttpGet(url);

        return httpClient.execute(getUrl, response -> {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return entity.getContent();
            } else {
                return null;
            }
        });
    }

    public void close() throws IOException {
        this.httpClient.close();
    }

    public void closeConnectionManager() throws IOException {
        this.connectionManager.close();
    }
}
