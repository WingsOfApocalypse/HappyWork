package com.happy.common.exception.error;

/**
 * 基础错误码,各业务领域错误码应继承该类
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月13日
 */
public class BaseErrorCode implements ErrorCode{

    /**
     * 错误码
     */
    private int         code;
    /**
     * 错误消息文本
     */
    protected String    message;
    /**
     * 业务领域
     */
    private ErrorDomain errorDomain;
    /**
     * 错误码对应的日志级别，系统错误码永远是error，业务错误可以根据需要显式提升级别
     */
    private ErrorLogLevel logLevel = ErrorLogLevel.ERROR;

    protected BaseErrorCode(int code, String message, ErrorDomain errorDomain){
        this.code = code;
        this.message = message;
        this.errorDomain = errorDomain;
        validateCode(code);
        initLevel();
    }

    /**
     * 验证错误码合法性
     *
     * @param code
     * @return void
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    protected void validateCode(int code) {
        if (!errorDomain.getCodeRange().contains(Math.abs(code))) {
            throw new IllegalArgumentException("code (" + code + ") is not valid as the valid range is "
                    + errorDomain.getCodeRange().toString());
        }
    }

    /**
     * 错误日志级别初始化,系统错误日志级别必须为error
     *
     * @param
     * @return void
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    protected void initLevel(){
        if (isSystemError()) {
            logLevel = ErrorLogLevel.ERROR;
        } else {
            logLevel = ErrorLogLevel.INFO;
        }
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getDomain() {
        return errorDomain.getDomain();
    }

    @Override
    public String getLocation() {
        return getClass().getName();
    }

    @Override
    public ErrorLogLevel getLogLevel() {
        return logLevel;
    }

    public BaseErrorCode setLogLevel(ErrorLogLevel logLevel) {
        if (logLevel != null) {
            this.logLevel = logLevel;
        }
        return this;
    }

    /**
     * 提升日志级别至warn
     *
     * @param
     * @return com.happy.common.exception.error.ErrorCode
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    protected ErrorCode toWarning(){
        if(!isSystemError()){
            logLevel = ErrorLogLevel.WARNING;
        }
        return this;
    }

    /**
     * 提升日志级别至error
     *
     * @param
     * @return com.happy.common.exception.error.ErrorCode
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    protected ErrorCode toError(){
        if(!isSystemError()){
            logLevel = ErrorLogLevel.ERROR;
        }
        return this;
    }

    /**
     * 判断是否为系统错误
     *
     * @param
     * @return boolean
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    private boolean isSystemError(){
        return code < 0;
    }
}
