package com.happy.common.exception.error;

/**
 * 错误码领域业务领域及范围
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月13日
 */
public class ApocalypseErrorDomain extends AbstractErrorDomain{

    /**
     * 基础通用错误码，目前范围：0~99
     */
    public static final ErrorDomain COMMON      = new ApocalypseErrorDomain("apocalypse", 0, 99);

    public ApocalypseErrorDomain(String domain, int codeRangeStart, int codeRangeEnd) {
        super(domain, codeRangeStart, codeRangeEnd);
    }

    @Override
    public ErrorDomain getParentDomain() {
        return null;
    }
}
