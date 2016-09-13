package com.happy.common.util.redis.exception;

import com.happy.common.exception.BizException;
import com.happy.common.exception.error.ErrorCode;

/**
 * redis操作异常类
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月12日
 */
public class RedisOperateException extends BizException {

    private static final long serialVersionUID = -5934370690715484184L;

    public RedisOperateException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public RedisOperateException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
