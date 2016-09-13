package com.happy.common.util.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis基本操作类
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月12日
 */
public class AbstractRedisOperation<K,V> {

    protected static final TimeUnit DEFAULT_TIME_UNIT   = TimeUnit.SECONDS;
    protected static final long     DEFAULT_EXPIRE_TIME = 60 * 60 * 24;
    // value中如果该标识在第二位，则说明存储的是Object
    private static final String     CLASS_TAG           = "@class";

    protected RedisTemplate<K, V>   redisTemplate;

    protected int                   dbIndex;
    private String                  storeModule;
}
