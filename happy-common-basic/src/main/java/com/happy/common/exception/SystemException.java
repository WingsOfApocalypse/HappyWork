package com.happy.common.exception;

import com.happy.common.exception.error.ErrorCode;

/**
 * 系统异常
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月13日
 */
public class SystemException extends ApocalypseRuntimeException{

    private static final long serialVersionUID = -1447546953036659349L;

    public SystemException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    @Override
    protected boolean needStackTrace(){
        return true;
    }

    protected void validateCode(int code){
        if (code >= 0) {
            //TODO 日志
        }
    }
}
