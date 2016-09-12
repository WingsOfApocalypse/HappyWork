package com.happy.common.util.memcache;

import com.happy.common.util.config.ConfigCenter;
import com.sun.deploy.cache.Cache;
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
            if(!checkSubConfig(namespaceArray[i], config)){
                return false;
            }
        }
        return init(namespaceArray);
    }

    private static boolean checkSubConfig(int namespace, ConfigCenter config) {
        String strNamespace = getNameSpaceStr(namespace);
        String hostConf = config.getValue("happy.cache." + strNamespace, "server");
        String portConf = config.getValue("happy.cache." + strNamespace, "port");
        if(StringUtils.isBlank(hostConf)){
            //TODO Derek 增加日志
            System.out.println("host conf is null");
            return false;
        }
        if(StringUtils.isBlank(portConf)){
            //TODO Derek 增加日志
            System.out.println("port conf is null");
            return false;
        }
        //TODO Derek 增加日志
        System.out.println("namespace=" + namespace + ". strNamespace="+strNamespace+", hostConf="+hostConf+", portConf=" + portConf);
        return true;
    }

    private synchronized static boolean init(int[] namespaceArray){
        if(isInit){
            return true;
        }
        try{
            for(int i=0;i<namespaceArray.length;i++){
                init(namespaceArray[i]);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        isInit = true;
        return true;
    }

    private static void init(int namespace) throws IOException {
        String strNamespace = getNameSpaceStr(namespace);
        final String hostConf = ConfigCenter.getInstance().getValue("happy.cache." + strNamespace, "server");
        final String portConf = ConfigCenter.getInstance().getValueWithDefault("happy.cache." + strNamespace, "port", "12121");

        String[][] serverArray = servers_common;
        if(StringUtils.isNotBlank(hostConf)){
            String[] hostArray = hostConf.split(",");
            String[] portArray = portConf.split(",");
            if(StringUtils.isNotBlank(portConf)){
                portArray = portConf.split(",");
            }
            serverArray = new String[hostArray.length][2];
            for(int i=0;i<hostArray.length;i++){
                serverArray[i][0] = hostArray[i];
                if(portArray==null){
                    serverArray[i][1]=servers_common[0][1];
                }else if(portArray.length > i){
                    serverArray[i][1]=portArray[i];
                }else{
                    serverArray[i][1] =portArray[0];
                }
            }
        }

        List<SpyMemcacheServer> servers = new ArrayList<SpyMemcacheServer>();
        for(int i=0;i<serverArray.length;i++){
            SpyMemcacheServer server = new SpyMemcacheServer();
            server.setIp(serverArray[i][0]);
            server.setPort(Integer.parseInt(serverArray[i][1]));
            server.setUsername(ConfigCenter.getInstance().getValueWithDefault("happy.cache." + strNamespace ,"username",""));
            server.setPassword(ConfigCenter.getInstance().getValueWithDefault("happy.cache." + strNamespace, "password", ""));
            servers.add(server);
        }

        if(CacheNamespaceEnum.COMMON.equals(namespace)){
            manager_common = new SpyMemcacheManager(servers);
            manager_common.connect();
        }else if(CacheNamespaceEnum.USER.equals(namespace)){
            manager_user = new SpyMemcacheManager(servers);
            manager_user.connect();
        }else if(CacheNamespaceEnum.OTHER.equals(namespace)){
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

    private static SpyMemcacheManager getManager(int namespace){
        if(CacheNamespaceEnum.COMMON.equals(namespace)){
            return manager_common;
        }else if (CacheNamespaceEnum.USER.equals(namespace)){
            return manager_user;
        }else if(CacheNamespaceEnum.OTHER.equals(namespace)){
            return manager_other;
        }else{
            return manager_common;
        }
    }

}
