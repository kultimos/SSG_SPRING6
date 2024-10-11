package com.kul.a10.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component  //加了@Component注解就是要通过spring代理进行Aop增强了
@Slf4j
public class MyAspect {

    @Before("execution(* com.kul.a10.service.MyService.*())")
    public void before() {
        log.info("before");
    }
}
