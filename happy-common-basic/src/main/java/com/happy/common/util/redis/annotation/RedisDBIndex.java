package com.happy.common.util.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.happy.common.util.redis.constant.RedisModuleEnum;

/**
 * redis数据库索引
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月13日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RedisDBIndex {

    RedisModuleEnum module();
}
