package com.kul.a06;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
public class MyConfig1 {
    /**
     * 当前Config1使用GenericApplicationContext类生产纯净版context,并配置对应注解的后处理器后
     * 但在运行后@Autowired和@PostConstruct还是失效
     *
     * 这是因为, @Configuration修饰的类,本质上相当于一个工厂对象;我们在config中定义的多个@Bean修饰的方法,实际上是一系列工厂方法
     * 那么在本类中
     * @param applicationContext
     */

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        log.info("注入applicationContext");
    }

    @PostConstruct
    public void init(){
        log.info("进行初始化");
    }

    @Bean
    public BeanFactoryPostProcessor processor1() {
        return beanFactory -> {
            log.info("执行processor1");
        };
    }
}
