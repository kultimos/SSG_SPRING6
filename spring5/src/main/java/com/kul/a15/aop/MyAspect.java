package com.kul.a15.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {
    /**
     * execution表达式方式
     */
    @Before("execution(* perform())")
    public void beforeMethod(){
        System.out.println("增强方法");
    }


    /**
     * Pointcut切入点方式
     */
    @Pointcut("execution(* perform())")
    public void pointCutMethod(){}

    @Before("pointCutMethod()")
    public void beforePointCutMethod(){
        System.out.println("通过切入点的写法进行增强");
    }
}
