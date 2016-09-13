package com.happy.common.exception;

import com.happy.common.exception.error.ErrorCode;
import com.happy.common.exception.error.ErrorLogLevel;

/**
 * 业务异常
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月13日
 */
public class BizException extends ApocalypseRuntimeException{

    private static final long serialVersionUID = -4092379401761109324L;

    private ErrorLogLevel     logLevel         = ErrorLogLevel.INFO;

    /**
     *
     * @param errorCode 错误码
     * @param message 异常消息
     */
    public BizException(ErrorCode errorCode, String message) {
        super(errorCode, message);
        logLevel = errorCode.getLogLevel();
    }

    /**
     *
     * @param errorCode 错误码
     * @param message 异常消息
     * @param cause Throwable异常实例对象
     */
    public BizException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
        logLevel = errorCode.getLogLevel();
    }

    @Override
    public ErrorLogLevel getErrorLogLevel(){
        return logLevel;
    }


    @Override
    protected void validateCode(int code){
        if (code < 0) {
            //TODO 日志
        }
    }

    /**
     * 业务异常丢弃堆栈信息
     *
     * @param
     * @return boolean
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    @Override
    protected boolean needStackTrace(){
        return false;
    }
}
