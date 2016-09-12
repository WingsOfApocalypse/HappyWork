package com.happy.common.util.memcache;

import com.happy.common.util.config.ConfigCenter;
import com.sun.deploy.cache.Cache;
import net.spy.memcached.CASResponse;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 缓存操作工具类
 *
 * Created by Derek on 16/9/12.
 */
public class CacheUtil {

    private static String[][]         servers_common = new String[][] { { "192.168.0.102", "12121" } };

    private static SpyMemcacheManager manager_common;
    private static SpyMemcacheManager manager_user;
    private static SpyMemcacheManager manager_other;

    private static boolean            isInit         = false;

    /**
     * 检查缓存配置项,同时初始化缓存
     *
     * @param namespaceArray 缓存命名空间集合
     * @return true:检查成功并初始化; 否则返回失败false
     */
    public synchronized static boolean checkConfigAndInit(int[] namespaceArray) {
        if (namespaceArray == null && namespaceArray.length == 0) {
            // TODO 日志,待日志模块接入后,添加
            System.out.println("The parameter namespaceArray is null");
            return false;
        }
        ConfigCenter config = ConfigCenter.getInstance();
        for (int i = 0; i < namespaceArray.length; i++) {
            if (!checkSubConfig(namespaceArray[i], config)) {
                return false;
            }
        }
        return init(namespaceArray);
    }

    private static boolean checkSubConfig(int namespace, ConfigCenter config) {
        String strNamespace = getNameSpaceStr(namespace);
        String hostConf = config.getValue("happy.cache." + strNamespace, "server");
        String portConf = config.getValue("happy.cache." + strNamespace, "port");
        if (StringUtils.isBlank(hostConf)) {
            // TODO Derek 增加日志
            System.out.println("host conf is null");
            return false;
        }
        if (StringUtils.isBlank(portConf)) {
            // TODO Derek 增加日志
            System.out.println("port conf is null");
            return false;
        }
        // TODO Derek 增加日志
        System.out.println("namespace=" + namespace + ". strNamespace=" + strNamespace + ", hostConf=" + hostConf
                           + ", portConf=" + portConf);
        return true;
    }

    private synchronized static boolean init(int[] namespaceArray) {
        if (isInit) {
            return true;
        }
        try {
            for (int i = 0; i < namespaceArray.length; i++) {
                init(namespaceArray[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        isInit = true;
        return true;
    }

    private static void init(int namespace) throws IOException {
        String strNamespace = getNameSpaceStr(namespace);
        final String hostConf = ConfigCenter.getInstance().getValue("happy.cache." + strNamespace, "server");
        final String portConf = ConfigCenter.getInstance().getValueWithDefault("happy.cache." + strNamespace, "port",
                                                                               "12121");

        String[][] serverArray = servers_common;
        if (StringUtils.isNotBlank(hostConf)) {
            String[] hostArray = hostConf.split(",");
            String[] portArray = portConf.split(",");
            if (StringUtils.isNotBlank(portConf)) {
                portArray = portConf.split(",");
            }
            serverArray = new String[hostArray.length][2];
            for (int i = 0; i < hostArray.length; i++) {
                serverArray[i][0] = hostArray[i];
                if (portArray == null) {
                    serverArray[i][1] = servers_common[0][1];
                } else if (portArray.length > i) {
                    serverArray[i][1] = portArray[i];
                } else {
                    serverArray[i][1] = portArray[0];
                }
            }
        }

        List<SpyMemcacheServer> servers = new ArrayList<SpyMemcacheServer>();
        for (int i = 0; i < serverArray.length; i++) {
            SpyMemcacheServer server = new SpyMemcacheServer();
            server.setIp(serverArray[i][0]);
            server.setPort(Integer.parseInt(serverArray[i][1]));
            server.setUsername(ConfigCenter.getInstance().getValueWithDefault("happy.cache." + strNamespace,
                                                                              "username", ""));
            server.setPassword(ConfigCenter.getInstance().getValueWithDefault("happy.cache." + strNamespace,
                                                                              "password", ""));
            servers.add(server);
        }

        if (CacheNamespaceEnum.COMMON.equals(namespace)) {
            manager_common = new SpyMemcacheManager(servers);
            manager_common.connect();
        } else if (CacheNamespaceEnum.USER.equals(namespace)) {
            manager_user = new SpyMemcacheManager(servers);
            manager_user.connect();
        } else if (CacheNamespaceEnum.OTHER.equals(namespace)) {
            manager_other = new SpyMemcacheManager(servers);
            manager_other.connect();
        }

    }

    private static String getNameSpaceStr(int namespace) {
        String strNamespace;
        if (CacheNamespaceEnum.COMMON.equals(namespace)) {
            strNamespace = "common";
        } else if (CacheNamespaceEnum.USER.equals(namespace)) {
            strNamespace = "user";
        } else if (CacheNamespaceEnum.OTHER.equals(namespace)) {
            strNamespace = "other";
        } else {
            strNamespace = "common";
        }
        return strNamespace;
    }

    private static SpyMemcacheManager getManager(int namespace) {
        if (CacheNamespaceEnum.COMMON.equals(namespace)) {
            return manager_common;
        } else if (CacheNamespaceEnum.USER.equals(namespace)) {
            return manager_user;
        } else if (CacheNamespaceEnum.OTHER.equals(namespace)) {
            return manager_other;
        } else {
            return manager_common;
        }
    }

    /**
     * 放进缓存 默认命名空间为COMMON
     *
     * @param key
     * @param value
     * @param expire
     */
    public static void put(String key, Object value, int expire) {
        put(CacheNamespaceEnum.COMMON.getType(), key, value, expire);
    }

    /**
     * 放进缓存
     *
     * @param namespace 命名空间
     * @param key 缓存key
     * @param value 缓存值
     * @param expire 有效时间
     * @return
     */
    public static boolean put(int namespace, String key, Object value, int expire) {
        // TODO Derek 增加日志
        if (getManager(namespace) == null) {
            return false;
        }

        try {
            if (key != null && value != null) {
                if (getManager(namespace).set(key, value, expire)) {
                    return true;
                }
            } else {
                System.out.println("Cache put Failed: Key=" + key + ", value=" + value);
            }
        } catch (Exception e) {
            System.out.println("Cache put Failed: Key=" + key);
        }
        return false;
    }

    public static boolean add(String key, Object value, int expire) {
        return add(CacheNamespaceEnum.COMMON.getType(), key, value, expire);
    }

    /**
     * 默认有效时间7天
     * 
     * @param key
     * @param value
     * @return
     */
    public static boolean add(String key, Object value) {
        return add(CacheNamespaceEnum.COMMON.getType(), key, value, CacheConstants.EXPIRED_60_60_24_7);
    }

    public static boolean add(int namespace, String key, Object value, int expire) {
        if (getManager(namespace) == null) {
            return false;
        }

        try {
            if (key != null && value != null) {
                return getManager(namespace).add(key, value, expire);
            } else {
                System.out.println("Cache put Failed: Key=" + key + ", value=" + value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        return get(CacheNamespaceEnum.COMMON.getType(), key);
    }

    /**
     * 获取缓存
     *
     * @param namespace
     * @param key
     * @return
     */
    public static Object get(int namespace, String key) {
        if (getManager(namespace) == null) {
            return null;
        }
        try {
            Object res = getManager(namespace).get(key);
            if (res != null) {
                System.out.println("get from cache: key=" + key);
            } else {
                System.out.println("not in cache: key=" + key);
            }
            return res;
        } catch (Exception e) {
            System.out.println("Cache Get Failed: key=" + key);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 清理缓存
     *
     * @param key
     * @return
     */
    public static boolean remove(String key) {
        return remove(CacheNamespaceEnum.COMMON.getType(), key);
    }

    /**
     * 清理缓存
     *
     * @param namespace
     * @param key
     * @return
     */
    public static boolean remove(int namespace, String key) {
        return removeByOrginKey(namespace, key);
    }

    /**
     * 清理缓存
     *
     * @param key
     * @return
     */
    public static boolean removeByOrginKey(String key) {
        return removeByOrginKey(CacheNamespaceEnum.COMMON.getType(), key);
    }

    /**
     * 清理缓存
     *
     * @param namespace
     * @param key
     * @return
     */
    public static boolean removeByOrginKey(int namespace, String key) {
        if (getManager(namespace) == null) {
            return false;
        }
        try {
            return getManager(namespace).delete(key);
        } catch (Exception e) {
            System.out.println("Cache Remove Failed: key=" + key);
        }
        return false;
    }

    /**
     * 缓存原子性取值
     *
     * @param key
     * @return
     */
    public static Object atomGet(String key) {
        return atomGet(CacheNamespaceEnum.COMMON.getType(), key);
    }

    public static Object atomGet(int namespace, String key) {
        if (getManager(namespace) == null) {
            return false;
        }
        try {
            Object res = getManager(namespace).atomGet(key);
            if (res != null) {
                System.out.println("atom get from cache: key=" + key);
            } else {
                System.out.println("not in cache: key=" + key);
            }
            return res;
        } catch (Exception e) {
            System.out.println("Cache Get Failed: key=" + key);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 缓存原子性新增操作
     *
     * @param key
     * @param value
     * @return
     */
    public static CASResponse cas(String key, Object value) {
        return cas(CacheNamespaceEnum.COMMON.getType(), key, value);
    }

    public static CASResponse cas(int namespace, String key, Object value) {
        if (getManager(namespace) == null) {
            return CASResponse.NOT_FOUND;
        }
        try {
            return getManager(namespace).cas(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return CASResponse.NOT_FOUND;
        }
    }

    /**
     * 缓存原子性新增操作
     *
     * @param key
     * @param expire
     * @param value
     */
    public static void cas(String key, int expire, String value) {
        cas(CacheNamespaceEnum.COMMON.getType(), key, value, expire);
    }

    public static void cas(int namespace, String key, String value, int expire) {
        if (getManager(namespace) == null) {
            return;
        }
        try {
            getManager(namespace).cas(key, value, expire);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存原子性递减
     *
     * @param key
     * @param value
     * @param expire
     */
    public static void decrement(String key, Integer value, int expire) {
        decrement(CacheNamespaceEnum.COMMON.getType(), key, value, expire);
    }

    public static void decrement(int namespace, String key, Integer value, int expire) {
        if (getManager(namespace) == null) {
            return;
        }
        try {
            getManager(namespace).decrement(key, value, expire);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存原子性递增
     *
     * @param key
     * @param value
     * @param expire
     */
    public static void increment(String key, Integer value, int expire) {
        increment(CacheNamespaceEnum.COMMON.getType(), key, value, expire);
    }

    public static void increment(int namespce, String key, Integer value, int expire) {
        if (getManager(namespce) == null) {
            return;
        }
        try {
            getManager(namespce).increment(key, value, expire);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有key
     *
     * @param namespace
     * @return
     */
    public static List<String> getAllKeys(int namespace) {
        if (getManager(namespace) == null) {
            return null;
        }
        try {
            return getManager(namespace).getAllKeys();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean flush() {
        return getManager(CacheNamespaceEnum.COMMON.getType()).flush();
    }

}
