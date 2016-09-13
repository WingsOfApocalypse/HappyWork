package com.happy.common.util.http;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * 访问服务的客户端配置
 * 
 * @author Derek.Wu
 * @Date 2016年09月13日
 */
public class ClientConfiguration {

    /**
     * 默认连接池最大HTTP连接个数,单位:个
     */
    public static final int DEFALUT_MAX_CONNECTIONS    = 1024;
    /**
     * 默认最大路由次数, 单位:次
     */
    public static final int DEFAULT_MAX_PER_ROUTE      = 10;
    /**
     * 默认建立http连接超时时间, 单位:毫秒
     */
    public static final int DEFAULT_CONNECTION_TIMEOUT = 3 * 1000;
    /**
     * 默认Socket传输数据超时时间, 单位:毫秒
     */
    public static final int DEFAULT_SOCKET_TIMEOUT     = 3 * 1000;

    /**
     * 默认建立http连接超时时间，单位：毫秒
     */
    private int             connectionTimeout          = DEFAULT_CONNECTION_TIMEOUT;
    /**
     * 默认socket传输数据超时时间，单位：毫秒
     */
    private int             socketTimeout              = DEFAULT_SOCKET_TIMEOUT;

    private PoolingHttpClientConnectionManager poolingConnMgr = null;

    /**
     * 构造新实例
     */
    public ClientConfiguration() {
    }

    /**
     * socket传输数据的超时时间, 单位: 毫秒
     *
     * @return int 通过打开的socket连接传输数据的超时时间
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public int getSocketTimeout() {
        return socketTimeout;
    }

    /**
     * 设置通过打开的socket连接传输数据的超时时间（单位：毫秒）。
     * 0表示无限等待（但不推荐使用）。
     *
     * @param socketTimeout
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    /**
     * 返回建立http连接超时时间，（单位：毫秒）。
     *
     * @param
     * @return int 建立http连接超时时间（单位：毫秒）。
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * 设置建立http连接超时时间（单位：毫秒）。
     * 
     * @param connectionTimeout 建立http连接超时时间（单位：毫秒）。
     * @return void
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public PoolingHttpClientConnectionManager getPoolingConnMgr() {
        return poolingConnMgr;
    }

    public void setPoolingConnMgr(PoolingHttpClientConnectionManager poolingConnMgr) {
        this.poolingConnMgr = poolingConnMgr;
    }
}
