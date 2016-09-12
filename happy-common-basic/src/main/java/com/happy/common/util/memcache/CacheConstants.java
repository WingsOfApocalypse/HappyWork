package com.happy.common.util.memcache;

/**
 * 缓存超市公共类
 *
 * Created by Derek on 16/9/12.
 */
public class CacheConstants {

    /**
     * 永久有效
     */
    public static final int EXPIRED_NEVER      = 0;
    /**
     * 一小时超时
     */
    public static final int EXPIRED_60_60      = 60 * 60;
    /**
     * 半天
     */
    public static final int EXPIRED_60_60_12   = 60 * 60 * 12;
    /**
     * 1天超时
     */
    public static final int EXPIRED_60_60_24   = 60 * 60 * 24;

    /**
     * 3天超时
     */
    public static final int EXPIRED_60_60_24_3 = 60 * 60 * 24 * 3;

    /**
     * 7天超时
     */
    public static final int EXPIRED_60_60_24_7 = 60 * 60 * 24 * 7;
}
