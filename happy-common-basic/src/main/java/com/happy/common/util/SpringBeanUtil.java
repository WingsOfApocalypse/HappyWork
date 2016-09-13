package com.happy.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 获取SpringBean对象的工具类
 *
 * @Author ApocalypseZhang
 * @Date 2016年09月13日
 */
@Component
@Lazy(false)
public class SpringBeanUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    /**
     * 注入applicationContext对象
     *
     * @param applicationContext
     * @throws BeansException
     * @return void
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtil.context = applicationContext;
    }

    /**
     * 获取bean实例对象
     *
     * @param beanName
     * @return java.lang.Object
     * @since v1.0.0
     * <PRE>
     * @author ApocalypseZhang
     * @Date 2016年09月13日
     * </PRE>
     */
    public static Object getBeanByName(final String beanName) {
        return SpringBeanUtil.context.getBean(beanName);
    }
}
