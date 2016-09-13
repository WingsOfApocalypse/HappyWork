package com.happy.common.util.redis.annotation;

import java.lang.reflect.Field;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.happy.common.util.redis.AbstractRedisOperation;
import com.happy.common.util.redis.constant.RedisDBEnum;
import com.happy.common.util.redis.error.RedisOperationErrorCode;
import com.happy.common.util.redis.exception.RedisOperateException;

/**
 * RedisDBIndex注解解析
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月13日
 */
@Component
public class RedisDBIndexProcessor implements BeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String s) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            RedisDBIndex dbIndex = field.getAnnotation(RedisDBIndex.class);
            if (dbIndex != null) {
                RedisDBEnum redisDBEnum = RedisDBEnum.getRedisDBByModule(dbIndex.module());
                if (redisDBEnum == null) {
                    throw new RedisOperateException(RedisOperationErrorCode.SELECT_DB_ERROR, "选择redis数据库异常，配置有误");
                }
                try {
                    AbstractRedisOperation<String, Object> operations;
                    operations = (AbstractRedisOperation<String, Object>) field.get(bean);
                    operations.setDbIndex(redisDBEnum.getDbIndex());
                } catch (Exception e) {
                    throw new RedisOperateException(RedisOperationErrorCode.GET_OPERATION_ERROR, "获取redis操作类异常", e);
                }
            }
        }
        return bean;
    }


    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
