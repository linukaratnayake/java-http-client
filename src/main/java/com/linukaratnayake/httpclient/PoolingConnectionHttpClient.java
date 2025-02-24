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

public class PoolingConnectionHttpClient extends HttpClient {
    private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;

    protected PoolingConnectionHttpClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        super(getPoolingHttpClientConnectionManager(false), false);   // isShared = false is the default
    }

    protected PoolingConnectionHttpClient(boolean isNew, boolean isShared) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        super(getPoolingHttpClientConnectionManager(isNew), isShared);
    }

    private static PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager(boolean isNew) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        if (isNew || poolingHttpClientConnectionManager == null || poolingHttpClientConnectionManager.isClosed()) {
            // To make thread safe
            synchronized (PoolingConnectionHttpClient.class) {
                // Check again as multiple threads can reach above step
                if (isNew || poolingHttpClientConnectionManager == null || poolingHttpClientConnectionManager.isClosed()) {
                    // Create a custom SSL context to trust all certificates (including self-signed)
                    SSLContextBuilder sslContextBuilder = SSLContexts.custom()
                            .loadTrustMaterial((chain, authType) -> true);  // Trust all certificates

                    // Create the PoolingHttpClientConnectionManager with SSL support
                    // Use the custom SSL context
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
                            .build();
                }
            }
        }

        return poolingHttpClientConnectionManager;
    }
}
