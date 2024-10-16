package org.springframework.aop.framework.autoproxy;

import com.kul.a13.Target;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.Order;

import java.util.List;

public class A17 {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("aspect1", Aspect1.class);
        context.registerBean("config", Config.class);
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.registerBean(AnnotationAwareAspectJAutoProxyCreator.class);
        context.refresh();

        /**
         * 找到对应类的可用的低级切面List
         */
        AnnotationAwareAspectJAutoProxyCreator creator = context.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
        List<Advisor> advisors = creator.findEligibleAdvisors(Target1.class, "target1");
        for (Advisor advisor : advisors) {
            System.out.println("切面信息:" + advisor);
        }

        Object o = creator.wrapIfNecessary(new Target1(), "Target1", "Target1");
        Object o1 = creator.wrapIfNecessary(new Target2(), "Target2", "Target2");
        System.out.println("target1代理对象信息: " + o.getClass());
        System.out.println("target2代理对象信息: " + o1.getClass());
        ((Target1)o).foo();

    }

    static class Target1 {
        public void foo () {
            System.out.println("target1 foo");
        }
    }

    static class Target2 {
        public void bar() {
            System.out.println("target2 bar");
        }
    }


    /**
     * 高级切面
     */
    @Aspect
    @Order(1)
    static class Aspect1 {

        @Before("execution(* foo())")
        public void before(){
            System.out.println("aspect1 before ...");
        }

        @After("execution(* foo())")
        public void after() {
            System.out.println("aspect1 after ...");
        }
    }

    /**
     * 低级切面
     */
    @Configuration
    static class Config{
        @Bean
        public Advisor advisor3(MethodInterceptor advice3) {
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* foo())");
            DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, advice3);
            advisor.setOrder(2);
            return advisor;
        }

        @Bean
        public MethodInterceptor advice3(){
            return new MethodInterceptor() {
                @Override
                public Object invoke(MethodInvocation invocation) throws Throwable {
                    System.out.println("advice3 before ...");
                    Object result = invocation.proceed();
                    System.out.println("advice3 after ...");
                    return result;
                }
            };
        };
    }
}
