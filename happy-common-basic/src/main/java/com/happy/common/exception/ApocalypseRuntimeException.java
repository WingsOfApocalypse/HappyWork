package com.happy.common.exception;

import java.util.Map;

import com.happy.common.exception.error.ErrorCode;
import com.happy.common.exception.error.ErrorLogLevel;

/**
 * 系统异常基类，继承于RuntimeException,根据异常的类型：是系统异常还是业务异常来选择不同的子类{@link SystemException}和{@link BizException}或其子类来使用，不要直接使用该基类
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月12日
 */
public class ApocalypseRuntimeException extends RuntimeException {


    private static final long   serialVersionUID = 6536676002067152567L;

    /**
     * 最终用于返回结果的错误码
     */
    private int                 errorCode;

    /**
     * 最终用于返回结果的错误文本
     */
    private String              errorText;

    /**
     * 错误码对象
     */
    private ErrorCode           errorCodeObj;

    /**
     * 格式化用于日志输出
     */
    private String              formattedErrorMsg;

    /**
     * 需要返回的错误数据（接口层会作为Result的data字段），用于client方做错误处理
     */
    private Object              errorData;

    /**
     * 保存错误消息文本中的动态变量
     */
    private Map<String, String> msgVariables;

    private static final String NO_SETTING_DOMAIN = "no setting errorDomain";

    private static final String NO_SETTING_LOCATION = "no setting errorLocation";


    public ApocalypseRuntimeException(ErrorCode errorCode, String message) {
        super(message);
        errorCodeObj = errorCode;
        initialize(errorCode.getCode());
    }

    public ApocalypseRuntimeException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        errorCodeObj = errorCode;
        initialize(errorCode.getCode());
    }

    private void initialize(int errorCode) {
        this.errorCode = errorCode;
        this.formattedErrorMsg = String.format("[domain:%s, code:%s, location:%s] - %s", getErrorDomain(), errorCode,
                getErrorLocation(), super.getMessage());
        validateCode(errorCode);
    }

    protected void validateCode(int code) {
        if (code >= 0) {
            //TODO 校验误用基类日志
        }
    }


    /**
     * 获取日志级别
     *
     * @param
     * @return com.happy.common.exception.error.ErrorLogLevel
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    public ErrorLogLevel getErrorLogLevel() {
        return ErrorLogLevel.ERROR;
    }

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public ApocalypseRuntimeException setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    /**
     * 根据开关控制是否填充stacktrace信息，对于业务类型异常不填充可以大大减少抛异常成本。
     *
     * @param
     * @return java.lang.Throwable
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    @Override
    public Throwable fillInStackTrace() {
        return needStackTrace() ? super.fillInStackTrace() : this;
    }

    /**
     * 是否需要stacktrace,默认是会带上，注意此方法必须返回静态变量，不能是动态变量
     *
     * @param
     * @return boolean
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    protected boolean needStackTrace() {
        return true;
    }

    /**
     * 返回格式化后的错误信息
     *
     * @param
     * @return java.lang.String
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    @Override
    public String getMessage() {
        return formattedErrorMsg;
    }

    /**
     * 错误码业务领域
     *
     * @param
     * @return java.lang.String
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    protected String getErrorDomain() {
        return errorCodeObj != null ? errorCodeObj.getDomain() : NO_SETTING_DOMAIN;
    }

    /**
     * 错误码所在类
     *
     * @param
     * @return java.lang.String
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    protected String getErrorLocation() {
        return errorCodeObj != null ? errorCodeObj.getLocation() : NO_SETTING_LOCATION;
    }
}
