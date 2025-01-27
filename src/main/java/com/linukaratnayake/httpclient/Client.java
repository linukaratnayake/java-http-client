package com.linukaratnayake.httpclient;

import org.apache.hc.client5.http.fluent.Content;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static Content get(String url, List<NameValuePair> params) {
        Content content = null;

        // Add to the request URL
        try {
            URI uri = new URIBuilder(new URI(url))
                    .addParameters(params)
                    .build();
            try {
                Response response = Request.get(uri.toString()).execute();
                content = response.returnContent();
            } catch (IOException e) {
                logger.error("An error occurred while processing the request", e);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return content;
    }
}
