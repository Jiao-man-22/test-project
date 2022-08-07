package com.jiao.testproject.testproject.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
@Log4j2
/**
 * 它设计的作用，如果需要在 Spring 容器完成 Bean 的实例化、配置和其他的初始化前后添加一些自己的逻辑处理，就可以定义一个或者多个 BeanPostProcessor 接口的实现，然后注册到容器中。
 */
public class SpringUtils  implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        log.info("完成 初始化Bean {} ：{}", beanName, bean.getClass().getName());
        return bean;
    }
}
