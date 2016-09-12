package com.happy.common.util.memcache;

/**
 * Created by Derek on 16/9/12.
 */
public enum CacheNamespaceEnum {

    /**
     * 缓存命名空间: 公共
     */
    COMMON(1, "公共"),
    /**
     * 缓存命名空间: 用户
     */
    USER(2, "用户"),
    /**
     * 缓存命名空间: 其它(以后扩展修改此处)
     */
    OTHER(3, "其它");

    public static SpyMemcacheManager manager_common;

    private int    type;

    private String desc;

    private SpyMemcacheManager manager;

    private CacheNamespaceEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return this.type;
    }

    public String getDesc() {
        return this.desc;
    }

    public boolean equals(int type) {
        return this.type == type;
    }

    public boolean equals(Integer type) {
        return type == null ? false : equals(type.intValue());
    }

    public boolean equals(CacheNamespaceEnum namespace) {
        return namespace == null ? false : equals(namespace.getType());
    }

    public static CacheNamespaceEnum getInstance(int type){
        for(CacheNamespaceEnum namespace: CacheNamespaceEnum.values()){
            if(namespace.equals(type)){
                return namespace;
            }
        }
        return null;
    }

    public static CacheNamespaceEnum getInstance(Integer type){
        for(CacheNamespaceEnum namespace: CacheNamespaceEnum.values()){
            if(namespace.equals(type)){
                return namespace;
            }
        }
        return null;
    }
}
