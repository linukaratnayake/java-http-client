package com.linukaratnayake.httpclient;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class PoolingConnectionHttpClientImpl extends HttpClientImpl {
    private static final int MAX_TOTAL_CONNECTIONS = 100;
    private static final int MAX_CONNECTIONS_PER_ROUTE = 5;

    private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;

    public PoolingConnectionHttpClientImpl() {
        super(getConnectionManager());
    }

    private static PoolingHttpClientConnectionManager getConnectionManager() {

        if (poolingHttpClientConnectionManager == null || poolingHttpClientConnectionManager.isClosed()) {
            // To make thread safe
            synchronized (PoolingConnectionHttpClientImpl.class) {
                // Check again as multiple threads can reach above step
                if (poolingHttpClientConnectionManager == null || poolingHttpClientConnectionManager.isClosed()) {
                    // Create a custom SSL context to trust all certificates (including self-signed)
                    SSLContextBuilder sslContextBuilder = null;  // Trust all certificates
                    try {
                        sslContextBuilder = SSLContexts.custom()
                                .loadTrustMaterial((chain, authType) -> true);
                    } catch (NoSuchAlgorithmException | KeyStoreException e) {
                        throw new RuntimeException(e);
                    }

                    // Create the PoolingHttpClientConnectionManager with SSL support
                    // Use the custom SSL context
                    try {
                        poolingHttpClientConnectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                                .setTlsSocketStrategy((TlsSocketStrategy) ClientTlsStrategyBuilder.create()
                                        .setSslContext(sslContextBuilder.build())  // Use the custom SSL context
                                        .setTlsVersions(TLS.V_1_3)
                                        .setSslContext(sslContextBuilder.build())
                                        .build())
                                .setDefaultSocketConfig(SocketConfig.custom()
                                        .setSoTimeout(Timeout.ofMinutes(1))
                                        .build())
                                .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
                                .setConnPoolPolicy(PoolReusePolicy.LIFO)
                                .setDefaultConnectionConfig(ConnectionConfig.custom()
                                        .setSocketTimeout(Timeout.ofMinutes(1))
                                        .setConnectTimeout(Timeout.ofMinutes(1))
                                        .setTimeToLive(TimeValue.ofMinutes(10))
                                        .build())
                                .setMaxConnPerRoute(MAX_CONNECTIONS_PER_ROUTE)
                                .setMaxConnTotal(MAX_TOTAL_CONNECTIONS)
                                .build();
                    } catch (NoSuchAlgorithmException | KeyManagementException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return poolingHttpClientConnectionManager;
    }
}
