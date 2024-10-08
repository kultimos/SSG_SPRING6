package com.kul.a07;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;

@Slf4j
public class Bean1 implements InitializingBean {

    @PostConstruct
    public void init1(){
        log.info("初始化1");
    }

    /**
     * InitializingBean提供的内置初始化方法
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("初始化2");
    }

    public void init3() {
        log.info("初始化3");
    }
}
