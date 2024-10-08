package com.kul.a03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class MyBeanPostProcessor implements InstantiationAwareBeanPostProcessor, DestructionAwareBeanPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(MyBeanPostProcessor.class);


    @Override
    public void postProcessBeforeDestruction(Object o, String s) throws BeansException {
        if(s.equals("lifeCycleBean")) {
            log.info("<<<<<<<销毁之前执行, 如 @PreDestoory");
        }
    }


    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if(beanName.equals("lifeCycleBean")) {
            log.info("<<<<<<<实例化之前执行, 这里返回的对象会替换掉原本的 bean");
        }
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if(beanName.equals("lifeCycleBean")) {
            log.info("<<<<<<<实例化之后执行, 这里如果返回false,则会跳过依赖注入阶段");
        }
        return true;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if(beanName.equals("lifeCycleBean")) {
            log.info("<<<<<<<依赖注入阶段执行, 如@Autowired、@Value、@Resource");
        }
        return pvs;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.equals("lifeCycleBean")) {
            log.info("<<<<<<<初始化之前执行, 这里返回的对象会替换掉原本的 bean, 如@PostConstruct、@ConfigurationProperties");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.equals("lifeCycleBean")) {
            log.info("<<<<<<<初始化之后执行, 这里返回的对象会替换掉原本的 bean, 如aop代理增强");
        }
        return bean;
    }
}
