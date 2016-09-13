package com.happy.common.util.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang.StringUtils;

/**
 * 序列化工具
 *
 * @author Derek.Wu
 * @Date 2016年09月13日
 */
public class SerializableTool {

    private SerializableTool() {
    }

    /**
     * 序列化
     * 
     * @param <T> object 待序列化的实例对象
     * @return java.lang.String 序列化的JSON字符串
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public static <T> String serialize(T object) {
        if (object == null) {
            return null;
        }
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 序列化
     * 
     * @param object 待序列化的实例对象
     * @return java.lang.String 序列化的JSON字符串
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public static <T> String serializeFormat(T object) {
        return serialize(object, SerializerFeature.PrettyFormat);
    }

    /**
     * 序列化
     *
     * @param object 待序列化的实例对象
     * @return java.lang.String 序列化的JSON字符串
     * @since v1.0.0
     *
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public static <T> String serializeDateFormat(T object) {
        return serialize(object, SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 序列化
     *
     * @param object 待序列化的实例对象
     * @return java.lang.String 序列化的JSON字符串
     * @since v1.0.0
     *
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public static <T> String serializeDateAndPrettyFormat(T object) {
        return serialize(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 序列化
     * 
     * @param object 待序列化的对象
     * @param features
     * @return java.lang.String
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    private static <T> String serialize(T object, SerializerFeature... features) {
        if (object == null) {
            return null;
        }
        try {
            return JSON.toJSONString(object, features);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 反序列化
     * 
     * @param jsonString 待反序列的json字符串
     * @param clazz 反序列化的目标对象的class
     * @return T 目标实例对象
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public static <T> T parseObject(String jsonString, Class<T> clazz) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        try {
            return JSON.parseObject(jsonString, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     *反序列化
     * @param jsonString 待反序列的json字符串
     * @param clazz 反序列化的目标对象的class
     * @return java.lang.Object 目标实例对象
     * @since v1.0.0
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public static Object deserialize(String jsonString, Class<?> clazz) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        try {
            if (StringUtils.startsWith(jsonString, "[")) {
                return JSON.parseArray(jsonString, clazz);
            } else {
                return JSON.parseObject(jsonString, clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
