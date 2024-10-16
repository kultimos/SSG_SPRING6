package com.kul.a15;

import com.kul.a13.Target;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.util.ArrayList;
import java.util.List;

/**
 * AOP中初核将一个具有增强功能的代理对象创建的流程
 */
public class A15 {
    public static void main(String[] args) {

        // 1.准备切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* foo())");

        // 2.准备通知
        MethodInterceptor advice = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                System.out.println("before前置增强");
                Object result = invocation.proceed(); //调用目标
                System.out.println("after后置增强");
                return result;
            }
        };

        // 3.准备切面
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, advice);

        // 4.创建代理
        ProxyFactory factory = new ProxyFactory();
        Target2 target1 = new Target2();
        factory.setTarget(target1);
        /**
         * 可以在代理对象上添加多个切面
         */
//        List<Advisor> advisorList = new ArrayList<>();
//        factory.addAdvisors(advisorList);
        factory.addAdvisor(advisor);
        factory.setProxyTargetClass(true);
        factory.setInterfaces(target1.getClass().getInterfaces());

        Target2 proxy = (Target2) factory.getProxy();
        System.out.println("看一下代理类的代理类型:" + proxy.getClass());
        proxy.foo();
        System.out.println();
        proxy.bar();


    }


    interface I1 {
        void foo();
        void bar();
    }

    static class Target1 implements I1 {

        @Override
        public void foo() {
            System.out.println("Target1 foo");
        }

        @Override
        public void bar() {
            System.out.println("Target1 bar");
        }
    }
    static class Target2 {


        public void foo() {
            System.out.println("Target2 foo");
        }


        public void bar() {
            System.out.println("Target2 bar");
        }
    }

}
