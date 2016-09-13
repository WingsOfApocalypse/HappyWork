package com.happy.common.util.http;

import org.apache.http.client.HttpClient;

import java.util.Map;

/**
 * Http客户端请求工具类
 *
 * @author Derek.Wu
 * @Date 2016年09月13日
 */
public class HttpClientUtil {

    private static final HttpRequestProxy httpRequestProxy = new HttpRequestProxy();

    private HttpClientUtil() {
    }

    /**
     * 发送GET请求
     *
     * @param url 请求url
     * @return java.lang.String 请求结果,响应内容
     * @since v1.0.0
     *
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public static String doGetRequest(String url) {
        return doGetRequest(url, null);
    }

    /**
     * 发送GET请求
     * 
     * @param url 请求url
     * @param headerMap 自定义头信息
     * @return java.lang.String 请求结果,响应内容
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public static String doGetRequest(String url, Map<String, String> headerMap) {
        HttpResult httpResult = httpRequestProxy.doGetRequest(url, headerMap);
        if (httpResult == null) {
            return null;
        } else if (httpResult.isSuccess()) {
            return httpResult.getContent();
        } else {
            return httpResult.getContent();
        }
    }

    /**
     * 发送POST请求
     * 
     * @param url 请求url
     * @param postData 请求数据BODY
     * @param headerMap 自定义头信息
     * @return com.happy.common.util.http.HttpResult 请求结果,相应内容
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public static HttpResult doPostRequest(String url, Map<String, Object> postData, Map<String, String> headerMap) {
        return httpRequestProxy.doPostRequest(url, postData, headerMap);
    }

}
