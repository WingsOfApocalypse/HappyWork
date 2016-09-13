package com.happy.common.exception.error;

import com.google.common.collect.Range;

/**
 * 业务领域基类
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月13日
 */
public abstract class AbstractErrorDomain implements ErrorDomain {

    private String domain;

    private Range<Integer> codeRange;

    /**
     * 获取错误域所在的父域，用于保留父子错误域的编译关系，方便查找，也用于验证
     *
     * @param
     * @return com.happy.common.exception.error.ErrorDomain
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    protected abstract ErrorDomain getParentDomain();

    /**
     * 声明错误域，注意：错误域的范围是正数范围，系统错误是负数但是范围检查只关心绝对值
     *
     * @param domain 风格类似java包声明方式，如mishi.order
     * @param codeRangeStart 错误码最小值
     * @param codeRangeEnd 错误码最大值
     */
    public AbstractErrorDomain(String domain, int codeRangeStart, int codeRangeEnd){
        this.domain = domain;
        this.codeRange = Range.closed(codeRangeStart, codeRangeEnd);
        validateRange(getCodeRange(), getParentDomain());
    }

    public String getDomain() {
        return domain;
    }

    public Range<Integer> getCodeRange() {
        return codeRange;
    }

    /**
     * 用于验证当前声明的错误码范围是否超出父域的范围
     *
     * @param codeRange
     * @param parentDomain
     * @return void
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    public static void validateRange(Range<Integer>  codeRange, ErrorDomain parentDomain ){
        if (null == parentDomain) {
            return;
        }
        if (!parentDomain.getCodeRange().encloses(codeRange)) {
            throw new IllegalArgumentException(String.format("invalid code range %s under the parent domain %s with range:%s",
                    codeRange, parentDomain.getDomain(),
                    parentDomain.getCodeRange()));
        }
    }
}
