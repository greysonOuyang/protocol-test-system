package com.yuyi.pts.common.util;

/**
 * @author : wzl
 * @date : 2021/4/13/15:29
 * @description:
 */


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component("applicationHelper")
@Slf4j
public class ApplicationHelper implements ApplicationContextAware {
    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationHelper.applicationContext = applicationContext;
    }

    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    /**
     * 获取bean
     *
     * @param name         bean的名字
     * @param requiredType java类类型
     * @param <T>          泛化类
     * @return bean
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    /**
     * 判断上下文中是否含有该名字的bean
     *
     * @param name bean名字
     * @return 布尔值
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * 判断bean是否是单例
     *
     * @param name
     * @return
     */
    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    /**
     * 获取java类型
     *
     * @param name
     * @return
     */
    public static Class<? extends Object> getType(String name) {
        return applicationContext.getType(name);
    }

    /**
     * @param key          关键字
     * @param defaultValue 默认
     * @return 启动环境参数
     */
    public static String getProperty(String key, String defaultValue) {
        return applicationContext.getEnvironment().getProperty(key, defaultValue);
    }

    /**
     * @param key 关键字
     * @return 启动环境参数
     */
    public static String getProperty(String key) {
        return applicationContext.getEnvironment().getProperty(key);
    }

    public static String getMessage(String messageKey, Object[] args) {

        try {
            //ckp 前端送，头里取，优化
            return ApplicationHelper.applicationContext.getMessage(messageKey, args, Locale.SIMPLIFIED_CHINESE);
        } catch (Throwable t) {
            log.error(t.getMessage());
        }
        return null;
    }

    public static String getMessage(String messageKey, Object[] args, String defaultMessage) {
        try {
            return ApplicationHelper.applicationContext.getMessage(messageKey, args, defaultMessage, Locale.SIMPLIFIED_CHINESE);
        } catch (Throwable t) {
            log.error(t.getMessage());
        }
        return null;
    }

}

