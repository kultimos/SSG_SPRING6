package com.kul.a01.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class TestBeanFactory {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Config.class).getBeanDefinition();
        beanFactory.registerBeanDefinition("config", beanDefinition);
        // BeanFactory只能解析config这个类,因为单单只是BeanFactory自己,功能实际上是没那么完善的;所以我们需要再添加一些后处理器,并启动他们
        // 通过这些后处理器来增强BeanFactory的功能


        // 添加一些常用的后处理器(包含BeanFactory的后置处理器和Bean的后置处理器),这里只是添加,还没有真正去运行这些后处理器;
        // 但是增加这行代码以后,就已经增加了几个bean的信息,
        // 比如: internalConfigurationAnnotationProcessor、internalAutowiredAnnotationProcessor、CommonAnnotationProcessor
        // 从名字也能看出,这三个bean一个是用来识别 @Configuration的、一个是用来识别@Autowired的、一个是用来识别@Resource注解的
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);

        // 获取这个BeanFactoryPostProcessor类中的所有后置处理器,然后逐一启动,这样就可以成功把我们自己定义的Component1和Component2成功识别
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values().stream().forEach(beanFactoryPostProcessor -> {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        });


        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for(String u : beanDefinitionNames) {
            System.out.println(u);
        }

        // 最初我们无法获取bean1中的bean2的原因是因为,这三个internalConfigurationAnnotationProcessor、internalAutowiredAnnotationProcessor、CommonAnnotationProcessor
        // Bean的后置处理器还没有被启动,所以没有识别我们@Autowired注解;
        // 但我们把Bean处理器也添加到BeanFactory中之后,就可以实现对@Autowired的识别了
        // 这一步是建立我们的BeanFactory和Bean后置处理器之间的联系
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().forEach(beanFactory::addBeanPostProcessor);

        //默认情况下,Spring的bean是懒加载,用到时才会真正初始化,但我们可以通过调用方法来实现预先初始化
        beanFactory.preInstantiateSingletons();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Bean1 bean1 = beanFactory.getBean(Bean1.class);
        System.out.println("我看看bean1里面情况" + bean1.getBean2());
    }

    // BeanFactoryPostProcessor是在Bean被实例化之前对Bean的定义信息进行补充和修改
    // BeanPoostProcessor是在Bean实例化完成之后,对Bean进行增强,如AOP;

    @Configuration
    static class Config {
        @Bean
        public Bean1 bean1(){
            return new Bean1();
        }

        @Bean
        public Bean2 bean2(){
            return new Bean2();
        }
    }

    static class Bean1{
        private static final Logger log = LoggerFactory.getLogger(Bean1.class);

        public Bean1(){
            log.debug("构造 Bean1()");
        }

        @Autowired
        private Bean2 bean2;

        public Bean2 getBean2() {
            return bean2;
        }
    }

    static class Bean2{
        private static final Logger log = LoggerFactory.getLogger(Bean2.class);

        public Bean2(){
            log.debug("构造 Bean2()");
        }
    }
}
