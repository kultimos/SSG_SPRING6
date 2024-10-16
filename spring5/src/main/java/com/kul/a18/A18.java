package org.springframework.aop.framework;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.*;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class A18 {

    static class Aspect {
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

        @AfterReturning("execution(* foo())")
        public void afterReturning() {
            System.out.println("afterReturning");
        }

        @AfterThrowing("execution(* foo())")
        public void afterThrowing(Exception e) {
            System.out.println("afterThrowing" + e.getMessage());
        }

        @Around("execution(* foo())")
        public Object around(ProceedingJoinPoint pjp) throws Throwable{
            return null;
        }
    }

    static class Target{
        public void foo() {
            System.out.println("target method foo");
        }
    }

    public static void main(String[] args) throws Throwable {
        AspectInstanceFactory factory = new SingletonAspectInstanceFactory(new Aspect());

        /**
         * 1.解析目标类的所有切面
         */
        List<Advisor> advisorList = new ArrayList<>();
        // 高级切面类转低级切面类
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
            } else if(method.isAnnotationPresent(AfterReturning.class)) {  // 解析方法上是否有@AfterReturning注解修饰
                String expression = method.getAnnotation(AfterReturning.class).value(); //解析@Before的value值
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);
                AspectJAfterReturningAdvice advice = new AspectJAfterReturningAdvice(method, pointcut, factory);
                // 低级切面
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                advisorList.add(advisor);
            } else if(method.isAnnotationPresent(Around.class)) {  // 解析方法上是否有@Around注解修饰
                String expression = method.getAnnotation(Around.class).value(); //解析@Before的value值
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);
                AspectJAroundAdvice advice = new AspectJAroundAdvice(method, pointcut, factory);
                // 低级切面
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                advisorList.add(advisor);
            }
        }

        /**
         * 此时我们的advisorList中就有多个注解解析后的切面了
         */
        for (Advisor advisor : advisorList) {
            System.out.println(advisor);
        }

        /**
         * 2.所有通知将统一转换为环绕通知 MethodInterceptor
         */
        Target target = new Target();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(target);
        /**
         * 下面这行代码作用,将MethodInvocation调用链信息放入当前线程中,这样无论是哪一层的环绕通知都可以从当前线程中获取调用链信息
         */
        proxyFactory.addAdvice(ExposeInvocationInterceptor.INSTANCE);
        proxyFactory.addAdvisors(advisorList);
        System.out.println("---------------------------------");
        /**
         * proxyFactory中提供了实现将统一转换为环绕通知的方法
         *
         * 从运行结果中不难看出,
         * AspectJAroundAdvice本身就是环绕通知
         * AspectJAfterReturningAdvice 替换
         */
        List<Object> methodInterceptorList = proxyFactory.getInterceptorsAndDynamicInterceptionAdvice(Target.class.getMethod("foo"), Target.class);
        for (Object o : methodInterceptorList) {
            System.out.println(o);
        }

        /**
         * 3.创建并执行调用链(调用链由"多个环绕通知"+"目标类信息"组成)
         */
        MethodInvocation methodInvocation = new ReflectiveMethodInvocation(
                null, target, Target.class.getMethod("foo"), new Object[0], Target.class, methodInterceptorList
        );

        /**
         * 执行调用链
         * proceed()方法会调用当前链中下一个环绕通知
         * 每个环绕通知内部继续调用proceed()
         * 调用到没有更多通知了,就调用目标方法
         */
        methodInvocation.proceed();
    }
}
