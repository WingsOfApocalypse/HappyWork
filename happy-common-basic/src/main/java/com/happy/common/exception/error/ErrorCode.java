package com.happy.common.exception.error;

/**
 * 错误码接口，异常处理模块直接依赖
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月12日
 */
public interface ErrorCode {

    /**
     * 错误码
     *
     * @param
     * @return int
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    int getCode();

    /**
     * 错误文本消息
     *
     * @param
     * @return java.lang.String
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    String getMessage();

    /**
     * 错误业务领域
     * 
     * @param 
     * @return java.lang.String
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang 
     * @Date 2016年09月13日
     * </PRE>
     */
    String getDomain();

    /**
     * 错误所在类,用于日志输出
     *
     * @param
     * @return java.lang.String
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    String getLocation();

    /**
     * 日志级别
     *
     * @param
     * @return com.happy.common.exception.error.ErrorLogLevel
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    ErrorLogLevel getLogLevel();

    /*======================================
     * 以下是静态常量
     * =====================================
     */

    /**
     * 错误码为0表示成功
     */
    int SYS_SUCCESS = 0;

    /**
     * 默认系统错误消息
     */
    String DEFAULT_SYS_ERROR_MSG = "系统内部错误，请稍后再试";
}
