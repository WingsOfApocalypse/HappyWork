package com.happy.common.util.http;

import org.apache.http.HttpStatus;

import java.util.Map;

/**
 * HttpClient 返回的结果
 *
 * @author Derek.Wu
 * @Date 2016年09月13日
 */
public class HttpResult {

    private int                 statusCode;

    private String              content;

    private Map<String, String> headerMap;

    public boolean isSuccess() {
        return statusCode == HttpStatus.SC_OK;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    @Override
    public String toString() {
        return "statusCode: " + this.statusCode + " content: " + content;
    }
}
