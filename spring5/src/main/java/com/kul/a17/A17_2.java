package com.kul.a17;

import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectInstanceFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class A17_2 {
    static class Aspect{
        @Before("execution(* foo())")
        public void before1() {
            System.out.println("before1");
        }

        @Before("execution(* foo())")
        public void before2() {
            System.out.println("before2");
        }

        public void after() {
            System.out.println("after");
        }
    }

    static class Target {
        public void foo() {
            System.out.println("target foo");
        }
    }

    /**
     * 高级切面转换为低级切面
     * @param args
     * @throws Throwable
     */
    public static void main(String []args) throws Throwable{
        // 代理工厂,需指明要代理的目标类类型
        AspectInstanceFactory factory = new SingletonAspectInstanceFactory(new Aspect());
        List<Advisor> advisorList = new ArrayList<>();
        for(Method method : Aspect.class.getDeclaredMethods()) {
            // 解析方法上是否有@Before注解修饰
            if (method.isAnnotationPresent(Before.class)) {
                String expression = method.getAnnotation(Before.class).value(); //解析@Before的value值
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);
                AspectJMethodBeforeAdvice advice = new AspectJMethodBeforeAdvice(method, pointcut, factory);

                // 低级切面
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                advisorList.add(advisor);
            }
        }
        for (Advisor advisor : advisorList) {
            System.out.println(advisor);
        }
    }
}
