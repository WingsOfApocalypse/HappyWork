package com.happy.common.exception.error;

import com.google.common.collect.Range;

/**
 * 错误域定义接口，区分错误码所在业务领域
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月13日
 */
public interface ErrorDomain {

    /**
     * 获取错误码业务领域
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
     * 获取错误码业务领域范围
     *
     * @param
     * @return Range<Integer>
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    Range<Integer> getCodeRange();
}
