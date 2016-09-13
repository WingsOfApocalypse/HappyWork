package com.happy.common.util.http;

import com.sun.deploy.util.SessionState;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * 创建httpclient
 * 
 * @author Derek.Wu
 * @Date 2016年09月13日
 */
public class HttpFactory {

    private static final Object                       lock           = new Object();

    private static PoolingHttpClientConnectionManager poolingConnMgr = null;

    private HttpFactory() {
    }

    private static PoolingHttpClientConnectionManager getDefaultPoolingConnMgr() {
        synchronized (lock) {
            if (poolingConnMgr == null) {
                poolingConnMgr = buildPoolingConnMgr(ClientConfiguration.DEFALUT_MAX_CONNECTIONS,
                                                     ClientConfiguration.DEFAULT_MAX_PER_ROUTE,
                                                     ClientConfiguration.DEFAULT_SOCKET_TIMEOUT);
            }
        }
        return poolingConnMgr;
    }

    public static PoolingHttpClientConnectionManager buildPoolingConnMgr(int maxSize, int maxPerRoute,
        int defalutSocketTimeout) {
        PoolingHttpClientConnectionManager poolingConnMgr = new PoolingHttpClientConnectionManager();
        // 连接池最大连接数
        poolingConnMgr.setMaxTotal(maxSize);
        // 最大路由次数
        poolingConnMgr.setDefaultMaxPerRoute(maxPerRoute);
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(defalutSocketTimeout).build();
        // Socket连接默认配置
        poolingConnMgr.setDefaultSocketConfig(socketConfig);
        return poolingConnMgr;
    }

    public static HttpClient createHttpClient() {
        return createHttpClient(new ClientConfiguration());
    }

    public static HttpClient createHttpClient(ClientConfiguration config) {
        // Set HTTP params
        if (config == null) {
            config = new ClientConfiguration();
        }
        PoolingHttpClientConnectionManager poolingConnMgr = config.getPoolingConnMgr() == null ? getDefaultPoolingConnMgr() : config.getPoolingConnMgr();

        RequestConfig requestConfig = RequestConfig.custom()
                                                   .setConnectTimeout(config.getConnectionTimeout())
                                                   .setSocketTimeout(config.getSocketTimeout())
                                                   .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                                                    .setConnectionManager(poolingConnMgr)
                                                    .setDefaultRequestConfig(requestConfig)
                                                    .build();
        return httpClient;
    }

}
