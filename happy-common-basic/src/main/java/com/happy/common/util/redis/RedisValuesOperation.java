package com.happy.common.util.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.happy.common.util.SpringBeanUtil;
import com.happy.common.util.redis.constant.RedisModuleEnum;

/**
 * redis非集合数据结构操作
 * 
 * @Author ApocalypseZhang
 * @Date 2016年09月13日
 */
public class RedisValuesOperation extends AbstractRedisOperation<String, Object> {

    public static RedisValuesOperation newInstance(RedisModuleEnum moduleEnum) {
        RedisValuesOperation redisValuesOperation = new RedisValuesOperation();
        redisValuesOperation.redisTemplate = (RedisTemplate<String, Object>) SpringBeanUtil.getBeanByName("redisTemplate");
        redisValuesOperation.setModule(moduleEnum.getValue());
        redisValuesOperation.generateRedisDBIndex();
        return redisValuesOperation;
    }

    /**
     * 向redis中添加对象,并设置过期时间
     * 只有key不存在时才设置,见setNX用法(set if Not eXists)
     *
     * @param key
     * @param value
     * @param expireTime
     * @return boolean
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    public boolean addObject(final String key, final Object value, final long expireTime) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.select(dbIndex);
                redisConnection.setNX(key.getBytes(),
                        ((RedisSerializer<Object>) redisTemplate.getValueSerializer()).serialize(value));
                if (expireTime > 0) {
                    redisConnection.expire(key.getBytes(), expireTime);
                }
                return true;
            }
        }, false, true);
    }

    /**
     * 向redis中添加对象,无过期时间
     *
     * @param key
     * @param value
     * @return boolean
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    public boolean addObject(final String key, final Object value) {
        return addObject(key, value, 0);
    }

    /**
     * 获取对象
     *
     * @param key
     * @return java.lang.Object
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    public Object getObject(final String key) {
        return redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.select(dbIndex);
                return redisTemplate.getValueSerializer().deserialize(redisConnection.get(key.getBytes()));
            }
        }, false, false);
    }

    /**
     * 获得对象
     *
     * @param key
     * @return
     */
    public <T> T get(final String key, final Class<T> clazz) {
        return clazz.cast(getObject(key));
    }

    /**
     * 向redis中添加字符串value,并设置过期时间
     * 只有key不存在时才设置,见setNX用法(set if Not eXists)
     *
     * @param key
     * @param value
     * @param expireTime
     * @return boolean
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    public boolean addString(final String key, final String value, final long expireTime) {
        return addObject(key, value, expireTime);
    }

    /**
     * 向redis中添加字符串,无过期时间
     *
     * @param key
     * @param value
     * @return boolean
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    public boolean addString(final String key, final String value) {
        return addString(key, value, 0);
    }

}
