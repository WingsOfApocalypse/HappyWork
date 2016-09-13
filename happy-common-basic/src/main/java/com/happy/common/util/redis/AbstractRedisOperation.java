package com.happy.common.util.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.happy.common.util.redis.constant.RedisDBEnum;
import com.happy.common.util.redis.constant.RedisModuleEnum;
import com.happy.common.util.redis.error.RedisOperationErrorCode;
import com.happy.common.util.redis.exception.RedisOperateException;

/**
 * redis基本操作类
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月12日
 */
public abstract class AbstractRedisOperation<K,V> {

    protected static final TimeUnit DEFAULT_TIME_UNIT   = TimeUnit.SECONDS;
    protected static final long     DEFAULT_EXPIRE_TIME = 60 * 60 * 24;
    // value中如果该标识在第二位，则说明存储的是Object
    private static final String     CLASS_TAG           = "@class";

    protected RedisTemplate<K, V>   redisTemplate;

    protected int                   dbIndex;
    private String                  module;

    protected void generateRedisDBIndex(){
        final RedisModuleEnum redisModule = RedisModuleEnum.getRedisModule(module);
        RedisDBEnum redisDB = RedisDBEnum.getRedisDBByModule(redisModule);
        if (redisDB == null) {
            throw new RedisOperateException(RedisOperationErrorCode.SELECT_DB_ERROR, "选择redis数据库异常，配置有误");
        }
        this.dbIndex = redisDB.getDbIndex();
    }

    /**
     * 删除单个key
     *
     * @param key
     * @return boolean
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    public boolean delete(final String key){
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.select(dbIndex);
                return redisConnection.del(key.getBytes());
            }
        });
        return result != null && result >= 1;
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return boolean
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    public boolean exists(final String key){
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.select(dbIndex);
                return redisConnection.exists(key.getBytes());
            }
        });
    }

    /**
     * 查找符合给定模式的key
     *
     * <PRE>
     * KEYS * 匹配数据库中所有 key 。
     * KEYS h?llo 匹配 hello ， hallo 和 hxllo 等。
     * KEYS h*llo 匹配 hllo 和 heeeeello 等。
     * KEYS h[ae]llo 匹配 hello 和 hallo ，但不匹配 hillo 。
     * </PRE>
     *
     * @param pattern
     * @return List<String>
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    public List<String> keys(final String pattern){

        return redisTemplate.execute(new RedisCallback<List<String>>() {
            @Override
            public List<String> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.select(dbIndex);
                Set<byte[]> set=redisConnection.keys(pattern.getBytes());
                List<String> keysList = new ArrayList<String>();
                for(byte[] byteSet:set){
                    keysList.add(new String(byteSet));
                }
                return keysList;
            }
        });

    }

    public RedisTemplate<K, V> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public int getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    protected byte[] serializeValue(Object value) {
        if (value instanceof String) {
            return ((String) value).getBytes();
        } else {
            return ((RedisSerializer<Object>) redisTemplate.getValueSerializer()).serialize(value);
        }
    }

    protected Object deserializeValue(byte[] value) {
        if (value == null) {
            return null;
        }
        String result = new String(value);
        if (result.indexOf(CLASS_TAG) != 2) {
            return result;
        }
        return ((RedisSerializer<Object>) redisTemplate.getValueSerializer()).deserialize(value);
    }
}
