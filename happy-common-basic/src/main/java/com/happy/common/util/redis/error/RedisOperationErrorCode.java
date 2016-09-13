package com.happy.common.util.redis.error;

import com.happy.common.exception.error.ApocalypseErrorDomain;
import com.happy.common.exception.error.BaseErrorCode;
import com.happy.common.exception.error.ErrorDomain;

/**
 * Redis操作错误码
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月13日
 */
public class RedisOperationErrorCode extends BaseErrorCode {

    /**
     * 生成redis key异常
     */
    public static final RedisOperationErrorCode GENERATE_KEY_ERROR = new RedisOperationErrorCode(-20001,"生成redis key异常", ApocalypseErrorDomain.COMMON);
    /**
     * 选择db异常
     */
    public static final RedisOperationErrorCode SELECT_DB_ERROR = new RedisOperationErrorCode(-20002,"选择redisDb异常", ApocalypseErrorDomain.COMMON);
    /**
     * 获取操作类异常
     */
    public static final RedisOperationErrorCode GET_OPERATION_ERROR = new RedisOperationErrorCode(-20003,"获取操作类异常", ApocalypseErrorDomain.COMMON);


    protected RedisOperationErrorCode(int code, String message, ErrorDomain errorDomain) {
        super(code, message, errorDomain);
    }
}
