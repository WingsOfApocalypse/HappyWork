package com.happy.common.util.redis.constant;

/**
 * redis数据库枚举
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月13日
 */
public enum RedisDBEnum {

    DEFAULT_RUN(0, RedisModuleEnum.DEFAULT_MODULE, "默认DB");

    private int             dbIndex;
    private RedisModuleEnum module;
    private String          desc;
    
    RedisDBEnum(int dbIndex, RedisModuleEnum module, String desc) {
        this.dbIndex = dbIndex;
        this.module = module;
        this.desc = desc;
    }

    public int getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }

    public RedisModuleEnum getModule() {
        return module;
    }

    public void setModule(RedisModuleEnum module) {
        this.module = module;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static RedisDBEnum getRedisDBByModule(RedisModuleEnum module){
        if(module==null){
            return null;
        }
        for(RedisDBEnum dbEnum:values()){
            if(dbEnum.getModule().equals(module)){
                return dbEnum;
            }
        }
        return null;
    }
}
