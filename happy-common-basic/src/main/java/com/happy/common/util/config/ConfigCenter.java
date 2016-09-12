package com.happy.common.util.config;

import com.happy.common.util.ClassHelper;
import com.happy.common.util.StreamUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 配置中心,配置文件读取
 *
 * Created by Derek on 16/9/12.
 */
public class ConfigCenter {

    public static final String  CONFIG_FILE_PATH        = "configCenter/happy_configCenter.properties";

    private static final String STREAM_CONFIG_FILE_PATH = "/" + CONFIG_FILE_PATH;

    private static Properties   prop                    = new Properties();

    private static File         configFile              = null;

    private static long         lastModifiedTime        = -1;

    private static class SingletonImpl {

        private static final ConfigCenter INSTANCE = new ConfigCenter();
    }

    private ConfigCenter() {
        init();
    }

    public static ConfigCenter getInstance() {
        return SingletonImpl.INSTANCE;
    }

    private void init() {
        try {
            Enumeration<URL> resourceUrls = ClassHelper.getClassLoader().getResources(CONFIG_FILE_PATH);
            int i = 0;
            while (resourceUrls.hasMoreElements()) {
                URL url = resourceUrls.nextElement();
                if (i == 0) {
                    configFile = new File(url.getPath());
                }
                i++;
            }
            if (configFile == null) {
                // TODO Derek 增加日志
                System.out.println("configFile is null.");
                return;
            }
            load();

        } catch (IOException e) {
            System.out.println("初始化配置中心配置文件异常");
            e.printStackTrace();
        }
    }

    private void load() {
        InputStream inStream = null;

        try {
            inStream = ConfigCenter.class.getResourceAsStream(STREAM_CONFIG_FILE_PATH);
            prop.load(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtils.closeStream(inStream);
        }

    }

    /**
     * 判断配置文件是否加载
     */
    public void reload() {
        if (configFile == null) {
            // TODO Derek 增加日志
            System.out.println("config file is null.");
            return;
        }
        if (lastModifiedTime != configFile.lastModified()) {
            load();
            lastModifiedTime = configFile.lastModified();
            // TODO Derek 日志
        } else {
            // TODO Derek 日志
        }
    }

    public String getValue(String groupName, String key) {
        if (groupName == null || key == null) {
            return null;
        }
        return prop.getProperty(getKey(groupName, key));
    }

    public String getValue(String key) {
        if (key == null) {
            return null;
        }
        return prop.getProperty(key);
    }

    public String getValueWithDefault(String key, String defalutValue) {
        if (key == null) {
            return defalutValue;
        }
        return prop.getProperty(key, defalutValue);
    }

    public String getValueWithDefault(String groupName, String key, String defalutValue) {
        if (groupName == null || key == null) {
            return defalutValue;
        }
        String value = prop.getProperty(getKey(groupName, key));
        if (StringUtils.isBlank(value)) {
            return defalutValue;
        } else {
            return value;
        }
    }

    public int getIntValueWithDefault(String key, int defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        String value = prop.getProperty(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public int getIntValueWithDefault(String groupName, String key, int defaultValue) {
        if (groupName == null || key == null) {
            return defaultValue;
        }
        String value = prop.getProperty(getKey(groupName, key));
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    private String getKey(String groupName, String key) {
        return groupName + "." + key;
    }
}
