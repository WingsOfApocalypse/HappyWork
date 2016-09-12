package com.happy.common.util.memcache;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Derek on 16/9/12.
 */
public class CacheUtilTest {

    @BeforeClass
    public static void setup(){
//        CacheUtil.checkConfigAndInit(new int[]{CacheNamespaceEnum.COMMON.getType(), CacheNamespaceEnum.USER.getType(), CacheNamespaceEnum.OTHER.getType()});
        CacheUtil.checkConfigAndInit(new int[]{CacheNamespaceEnum.COMMON.getType()});
    }

    @Test
    public void test(){
        CacheUtil.put(CacheNamespaceEnum.COMMON.getType(), "key_derek", "WQ", CacheConstants.EXPIRED_60_60);
        System.out.println(CacheUtil.get(CacheNamespaceEnum.COMMON.getType(), "key_derek"));
    }

}
