package com.happy.common.util.http;

import com.happy.common.util.json.SerializableTool;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * http 请求代理类, 支持链接池 定时清理空闲链接
 * 
 * @author Derek.Wu
 * @Date 2016年09月13日
 */
public class HttpRequestProxy {

    private static final String   HEADER_SEPRATOR = ";";

    protected ClientConfiguration config;

    /**
     * <构造函数>
     * 
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu 
     * @Date
     * </PRE>
     */
    public HttpRequestProxy() {
        this(new ClientConfiguration());
    }

    /**
     * <构造函数>
     *
     * 其中通过setSocketTimeout()方法设置打开的socket连接传输数据的超时时间（单位：毫秒）；0表示无限等待（但不推荐使用）；
     * 通过setConnectionTimeout()方法设置建立HTTP连接的超时时间（单位：毫秒）；
     *
     * @param config 配置项实例对象
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu 
     * @Date
     * </PRE>
     */
    public HttpRequestProxy(ClientConfiguration config) {
        this.config = config == null ? new ClientConfiguration() : config;
    }

    /**
     * <构造函数>
     *
     * 0表示无限等待（但不推荐使用）；
     * 如果要设置建立HTTP连接的超时时间，请使用其他构造函数；
     *
     * @param socketTimeout 打开的socket连接传输数据的超时时间（单位：毫秒）
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu 
     * @Date
     * </PRE>
     */
    public HttpRequestProxy(int socketTimeout) {
        config = new ClientConfiguration();
        config.setSocketTimeout(socketTimeout);
    }

    /**
     * 设置打开的socket连接传输数据的超时时间（单位：毫秒）；0表示无限等待（但不推荐使用）；
     * 注意：设置后，该实例对象后续所有请求都使用相同的配置；
     *
     * @param socketTimeout socket连接传输数据的超时时间（单位：毫秒）
     * @return com.happy.common.util.http.HttpRequestProxy
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu 
     * @Date 2016-09-13
     * </PRE>
     */
    public HttpRequestProxy setSocketTimeout(int socketTimeout) {
        this.config.setSocketTimeout(socketTimeout);
        return this;
    }

    /**
     * 设置建立HTTP连接的超时时间（单位：毫秒）；
     * 注意：设置后，该实例对象后续所有请求都使用相同的配置；
     * 
     * @param connectTimeout 建立HTTP连接的超时时间（单位：毫秒）
     * @return com.happy.common.util.http.HttpRequestProxy
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu 
     * @Date 2016-09-13
     * </PRE>
     */
    public HttpRequestProxy setConnectionTimeout(int connectTimeout) {
        this.config.setConnectionTimeout(connectTimeout);
        return this;
    }

    protected HttpClient getHttpClient() {
        return HttpFactory.createHttpClient(this.config);
    }

    /**
     * GET 请求
     *
     * @param url 请求url
     * @return HttpResult 请求结果，响应内容
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public HttpResult doGetRequest(String url) {
        return doGetRequest(url, null);
    }

    /**
     * GET 请求
     * 
     * @param url 请求url
     * @param headerMap 自定义请求头，没有传null
     * @return com.happy.common.util.http.HttpResult 请求结果，响应内容
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public HttpResult doGetRequest(String url, Map<String, String> headerMap) {
        // 头部请求信息
        // 设置为get取连接的方式
        HttpGet get = new HttpGet(url);
        addHeader(get, headerMap);
        return executeMethod(get);
    }

    /**
     * POST 请求
     * 
     * @param url 请求url
     * @param postData 需要递交的数据，即请求的HTTP BODY数据，没有传null
     * @param headerMap 自定义请求头，没有传null
     * @return com.happy.common.util.http.HttpResult 请求结果，响应内容
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public HttpResult doPostRequest(String url, Map<String, Object> postData, Map<String, String> headerMap) {
        HttpPost httpPost = new HttpPost(url);
        // 获取HTTP Body 编码格式, 并设置请求头
        String charset = getPostCharsetAndAddHeader(httpPost, headerMap);
        // 获取请求实体
        HttpEntity entity = getFormEntity(postData, charset);
        httpPost.setEntity(entity);
        return executeMethod(httpPost);
    }

    /**
     * POST 请求
     * 
     * @param url 请求url
     * @param postData 需要递交的数据，即请求的HTTP BODY数据，没有传null
     * @param headerMap 自定义请求头，没有传null
     * @return com.happy.common.util.http.HttpResult 请求结果，响应内容
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu 
     * @Date 2016-09-13
     * </PRE>
     */
    public HttpResult doPostRequest(String url, String postData, Map<String, String> headerMap) {
        HttpPost httpPost = new HttpPost(url);
        // 获取HTTP Body 编码格式, 并设置请求头
        String charset = getPostCharsetAndAddHeader(httpPost, headerMap);

        if(postData!=null && !postData.isEmpty()){
            try{
                httpPost.setEntity(new StringEntity(postData, charset));
            }catch (Exception e){
                //TODO Derek
                e.printStackTrace();
            }
        }
        return executeMethod(httpPost);
    }

    /**
     * PUT 请求
     * @param url 请求url
     * @param postData 请求的HTTP BODY数据
     * @param headerMap 自定义请求头
     * @return com.happy.common.util.http.HttpResult 请求结果，响应内容
     * @since v1.0.0
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public HttpResult doPutRequest(String url, String postData, Map<String, String> headerMap){
        HttpPut httpPut = new HttpPut(url);
        String charset = getPostCharsetAndAddHeader(httpPut, headerMap);
        if(postData!=null && !postData.isEmpty()){
            try{
                httpPut.setEntity(new StringEntity(postData, charset));
            }catch (Exception e){
                //TODO Derek
                e.printStackTrace();
            }
        }
        return executeMethod(httpPut);
    }

    /**
     * DELETE 请求
     *
     * @param url 请求url
     * @param headerMap 自定义请求头
     * @return com.happy.common.util.http.HttpResult 请求结果,响应内容
     * @since v1.0.0
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    public HttpResult doDeleteRequest(String url, Map<String, String> headerMap){
        HttpDelete httpDelete = new HttpDelete(url);
        addHeader(httpDelete, headerMap);
        return executeMethod(httpDelete);
    }

    /**
     * 添加自定义请求头
     * 
     * @param httpMessage 请求消息实例对象
     * @param headerMap
     * @return void
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu 
     * @Date 2016-09-13
     * </PRE>
     */
    protected void addHeader(AbstractHttpMessage httpMessage, Map<String, String> headerMap) {
        if (headerMap == null) {
            headerMap = new HashMap<String, String>();
        }
        String value = null;

        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            if (StringUtils.isBlank(entry.getKey())) {
                continue;
            }
            value = entry.getValue();
            if (StringUtils.isBlank(value)) {
                continue;
            }
            if (value.contains(HEADER_SEPRATOR)) {
                String[] subValues = value.split(HEADER_SEPRATOR);
                for (int i = 0; i < subValues.length; i++) {
                    httpMessage.addHeader(entry.getKey(), subValues[i]);
                }
            } else {
                httpMessage.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 获取编码格式并设置请求头
     * 
     * @param httpMessage 请求消息实例对象
     * @param headerMap 请求头
     * @return java.lang.String 编码格式
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu 
     * @Date 2016-09-13
     * </PRE>
     */
    protected String getPostCharsetAndAddHeader(AbstractHttpMessage httpMessage, Map<String, String> headerMap) {
        if (headerMap == null) {
            headerMap = new HashMap<String, String>();
        }
        String charset = headerMap.get(HttpHeaderConstant.CHARSET);

        // 设置Body请求编码格式
        if (StringUtils.isEmpty(charset)) {
            charset = HttpHeaderConstant.CHARSET_UTF8;
            headerMap.put(HttpHeaderConstant.CHARSET, charset);
        }

        // 设置请求Body呢哦荣格式以及编码格式
        if (StringUtils.isEmpty(headerMap.get(HttpHeaderConstant.CONTENT_TYPE))) {
            headerMap.put(HttpHeaderConstant.CONTENT_TYPE, HttpHeaderConstant.CONTENT_TYPE_FORM);
        }
        addHeader(httpMessage, headerMap);
        return charset;
    }

    protected HttpResult executeMethod(HttpRequestBase httpRequestBase) {
        // TODO Derek 异常的处理,日后再添加
        HttpResult httpResult = new HttpResult();
        HttpResponse response = null;
        try {
            response = this.getHttpClient().execute(httpRequestBase);
            httpResult.setHeaderMap(getResponseHeaders(response));
            httpResult.setStatusCode(response.getStatusLine().getStatusCode());
            httpResult.setContent(getResponseContent(response.getEntity()));
            return httpResult;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    // Ensures that the entity content is fully consumed and the content stream, if exists, is closed.
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    // do nothing
                }
            }
            // 连接超时, 关闭TCP 连接, remove leased队列
            httpRequestBase.releaseConnection();
        }
        return null;
    }

    /**
     * 获取 Http Response的头信息
     * 
     * @param response
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    protected Map<String, String> getResponseHeaders(HttpResponse response) {
        Map<String, String> headerMap = new HashMap<String, String>();
        String name = null;
        String value = null;
        for (Header header : response.getAllHeaders()) {
            name = header.getName();
            value = StringUtils.trim(header.getValue());
            if (StringUtils.isBlank(name) || StringUtils.isBlank(value)) {
                continue;
            }
            if ("Set-Cookie".equalsIgnoreCase(name)) {
                value = parseCookies(headerMap.get("Cookie"), value);
                if (StringUtils.isNotBlank(value)) {
                    headerMap.put("Cookie", value);
                }
            } else if (headerMap.get(name) == null) {
                headerMap.put(name, value);
            } else {
                value = headerMap.get(name) + HEADER_SEPRATOR + value;
                headerMap.put(name, value);
            }
        }
        return headerMap;
    }

    /**
     * 读取响应内容 支持读取压缩过的响应内容
     * 
     * @param entity
     * @return java.lang.String
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    protected String getResponseContent(HttpEntity entity) throws IOException {
        HttpEntity tempEntity = getEntityWithZip(entity);
        return EntityUtils.toString(tempEntity, "UTF-8");
    }

    protected HttpEntity getEntityWithZip(HttpEntity entity) {
        HttpEntity tempEntity = null;

        // 请求返回的内容数据压缩格式
        Header encodingHeader = entity.getContentEncoding();
        if (encodingHeader != null) {
            // 判断响应内容是否压缩过
            for (HeaderElement element : encodingHeader.getElements()) {
                if ("gzip".equalsIgnoreCase(element.getName())) {
                    tempEntity = new GzipDecompressingEntity(entity);
                    break;
                }
            }
        }
        return tempEntity;
    }

    /**
     * 解析Cookie信息
     *
     * @param existValue
     * @param value
     * @return
     */
    private String parseCookies(String existValue, String value) {
        if (StringUtils.isBlank(value)) {
            return existValue;
        }
        int sep_idx = value.indexOf(";");
        if (sep_idx == -1) {
            return existValue;
        }
        String[] subValues = value.split(";");
        if (StringUtils.isBlank(subValues[0]) || "null".equals(StringUtils.trim(subValues[0]))) {
            return existValue;
        }
        if (StringUtils.isBlank(existValue)) {
            return StringUtils.trim(subValues[0]);
        } else {
            return existValue + ";" + StringUtils.trim(subValues[0]);
        }
    }

    /**
     * 获取请求实体
     * 
     * @param postData 请求数据
     * @param charset 编码格式
     * @return org.apache.http.HttpEntity 请求实体
     * @since v1.0.0
     * 
     * <PRE>
     * @author Derek.Wu
     * @Date 2016-09-13
     * </PRE>
     */
    private HttpEntity getFormEntity(Map<String, Object> postData, String charset) {
        HttpEntity entity = null;
        if (postData != null && !postData.isEmpty()) {
            Object value = null;
            ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry : postData.entrySet()) {
                value = entry.getValue();
                if (StringUtils.isEmpty(entry.getKey()) || value == null) {
                    continue;
                }
                nvps.add(new BasicNameValuePair(entry.getKey(), getPostStringValue(value)));
            }
            try {
                entity = new UrlEncodedFormEntity(nvps, charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

    private String getPostStringValue(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return (String) value;
        } else if ((value instanceof Integer) || (value instanceof Long) || (value instanceof Boolean)) {
            return value.toString();
        } else {
            return SerializableTool.serialize(value);
        }
    }

}
