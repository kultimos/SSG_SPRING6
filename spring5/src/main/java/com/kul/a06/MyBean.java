package com.kul.a06;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
public class MyBean implements BeanNameAware, ApplicationContextAware, InitializingBean {
    /**
     * 根据打印信息可以看到,先进行了Aware接口的回调,再进行初始化方法的回调
     */

    /**
     * 当Bean通过context.registerBean("myBean", MyBean.class);创建并指定beanName时,就会回调BeanNameAware的方法来setBeanName;
     * @param name
     */
    @Override
    public void setBeanName(String name) {
        log.info("当前bean{}, 名字是: {}", this, name);
    }

    /**
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("当前bean{}, 容器是: {}", this, applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("当前bean{}进行初始化", this);
    }

    @Autowired
    public void setByAutowired(ApplicationContext applicationContext) {
        log.info("使用Autowire注入时,当前bean{}, 容器是: {}", this, applicationContext);
    }

    @PostConstruct
    public void init() {
        log.info("当前bean{},使用@PostConstruct注解进行初始化", this);
    }

}
