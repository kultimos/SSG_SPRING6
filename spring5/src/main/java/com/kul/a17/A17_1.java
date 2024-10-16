package com.kul.a17;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;
import java.sql.SQLOutput;

public class A17_1 {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean(ConfigurationClassPostProcessor.class); // 解析@Bean注解的后置处理器
        context.registerBean(Config.class);
        context.refresh();
        context.close();
    }


    @Configuration
    static class Config{
        @Bean
        public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator(){
            return new AnnotationAwareAspectJAutoProxyCreator();
        }

        @Bean
        public AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor(){
            return new AutowiredAnnotationBeanPostProcessor();
        }

        @Bean
        public CommonAnnotationBeanPostProcessor commonAnnotationBeanPostProcessor() {
            return new CommonAnnotationBeanPostProcessor();
        }

        @Bean
        public Advisor advisor(MethodInterceptor advice){
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* foo())");
            return new DefaultPointcutAdvisor(pointcut, advice);
        }

        @Bean
        public MethodInterceptor advice(){
            return (MethodInvocation invocation) -> {
                System.out.println("before");
                return invocation.proceed();
            };
        }

        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2(){
            return new Bean2();
        }

        static class Bean1{
            public void foo(){}

            public Bean1(){
                System.out.println("Bean1()");
            }

            @Autowired
            public void setBean2(Bean2 bean2) {
                System.out.println("Bean1 setBean2 class is " + bean2.getClass());
            }

            @PostConstruct
            public void init(){
                System.out.println("Bean1 init()");
            }
        }

        static class Bean2 {


            public Bean2(){
                System.out.println("Bean2()");
            }

            /**
             * 可以发现,此时自动注入的Bean1已经是代理对象了,这是因为后续如果Bean2希望使用Bean1,肯定也是希望使用Bean1增强后的方法
             * @param bean1
             */
            @Autowired
            public void setBean1(Bean1 bean1) {
                System.out.println("Bean2 setBean1 class is " + bean1.getClass());
            }

            @PostConstruct
            public void init(){
                System.out.println("Bean2 init()");
            }
        }
    }
}
