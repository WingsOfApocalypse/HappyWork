package com.happy.common.util.redis.constant;

import org.apache.commons.lang.StringUtils;

/**
 * redis数据模块
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月13日
 */
public enum RedisModuleEnum {

    DEFAULT_MODULE("default"),
    USER("user");

    private String value;

    RedisModuleEnum(String value) {
        this.value = value;
    }
    
    public static RedisModuleEnum getRedisModule(String module) {
        if (StringUtils.isBlank(module)) {
            return DEFAULT_MODULE;
        }
        for (RedisModuleEnum moduleEnum : values()) {
            if (moduleEnum.value.equalsIgnoreCase(module)) {
                return moduleEnum;
            }
        }
        return DEFAULT_MODULE;
    }

    public boolean equals(String module) {
        if (StringUtils.isBlank(module)) {
            return false;
        }
        return this.value.equals(module);
    }

    public boolean equals(RedisModuleEnum module) {
        if (module == null) {
            return false;
        }
        return equals(module.getValue());
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
