package com.happy.common.util.redis.util;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.collect.Lists;
import com.happy.common.util.redis.constant.RedisDBEnum;
import com.happy.common.util.redis.constant.RedisModuleEnum;
import com.happy.common.util.redis.error.RedisOperationErrorCode;
import com.happy.common.util.redis.exception.RedisOperateException;

/**
 * redisKey管理
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月13日
 */
public class RedisKeyManager {

    private List<RedisKeyBuilder> builders;

    private final RedisModuleEnum module;
    private final String          field;
    private final Lock            lock = new ReentrantLock();

    private RedisKeyManager(RedisModuleEnum module, String field) {
        this.module = module;
        this.field = field;
        this.builders = Lists.newArrayList();
    }

    public static final RedisKeyManager newInstance(RedisModuleEnum module, String field) {
        return new RedisKeyManager(module, field);
    }

    public RedisKeyManager index(final String indexName, final Object indexValue) {
        RedisKeyBuilder builder = new RedisKeyBuilder(indexName, indexValue);
        lock.lock();
        try {
            if (builders == null) {
                builders = Lists.newArrayList();
            }
        } finally {
            lock.unlock();
        }
        builders.add(builder);
        return this;
    }

    /**
     * 生成redisKey
     * ep:default:key1:value1:key2:value2:field
     *
     * @param
     * @return java.lang.String
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    public String generate() {
        try {
            StringBuffer buffer = generatePrefix();
            for (RedisKeyBuilder redisKeyBuilder : builders) {
                buffer.append(redisKeyBuilder.indexName).append(":").append(redisKeyBuilder.indexValue).append(":");
            }
            buffer.append(field);
            return buffer.toString();
        } finally {
            // 防止OOM，使用完就清空
            builders = null;
        }
    }

    /**
     * 生成redisKey前缀,目前只根据module生成前缀
     * ep:默认module则前缀为default:
     *
     * @param
     * @return java.lang.StringBuffer
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    private StringBuffer generatePrefix() {
        RedisDBEnum redisDBEnum = RedisDBEnum.getRedisDBByModule(module);
        if (redisDBEnum == null) {
            throw new RedisOperateException(RedisOperationErrorCode.GENERATE_KEY_ERROR, "redis生成KEY异常");
        }
        return new StringBuffer(redisDBEnum.getModule().getValue()).append(":");
    }


    private class RedisKeyBuilder {

        private final String indexName;
        private final Object indexValue;

        private RedisKeyBuilder(final String indexName, final Object indexValue) {
            this.indexName = indexName;
            this.indexValue = indexValue;
        }
    }
}
