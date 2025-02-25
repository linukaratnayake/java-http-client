package com.linukaratnayake.httpclient;

import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class BasicConnectionHttpClientImpl extends HttpClientImpl {
    private static BasicHttpClientConnectionManager basicHttpConnectionManager = null;

    protected BasicConnectionHttpClientImpl() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        super(getBasicHttpClientConnectionManager(false), false);   // isShared = false is the default
    }

    protected BasicConnectionHttpClientImpl(boolean isNew, boolean isShared) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        super(getBasicHttpClientConnectionManager(isNew), isShared);
    }

    private static BasicHttpClientConnectionManager getBasicHttpClientConnectionManager(boolean isNew) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        if (isNew || basicHttpConnectionManager == null || basicHttpConnectionManager.isClosed()) {
            // To make thread safe
            synchronized (BasicConnectionHttpClientImpl.class) {
                // Check again as multiple threads can reach above step
                if (isNew || basicHttpConnectionManager == null || basicHttpConnectionManager.isClosed()) {
                    final TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
                    final SSLContext sslContext = SSLContexts.custom()
                            .loadTrustMaterial(null, acceptingTrustStrategy)
                            .build();
                    final SSLConnectionSocketFactory sslsf =
                            new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
                    final Registry<ConnectionSocketFactory> socketFactoryRegistry =
                            RegistryBuilder.<ConnectionSocketFactory>create()
                                    .register("https", sslsf)
                                    .register("http", new PlainConnectionSocketFactory())
                                    .build();

                    basicHttpConnectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
                }
            }
        }
        return basicHttpConnectionManager;
    }
}
