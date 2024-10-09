package com.kul.a09;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Slf4j
@ComponentScan("com.kul.a09")
public class A09Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(A09Application.class);
        E e = context.getBean(E.class);
        log.info("看看对象的类型:{}", e.getF1().getClass()); //会发现是一个CGlib的代理对象
        log.info("{}", e.getF1());
        log.info("{}", e.getF1());
        log.info("{}", e.getF1());

        log.info("看看对象的类型:{}", e.getF2().getClass()); //会发现是一个CGlib的代理对象
        log.info("{}", e.getF2());
        log.info("{}", e.getF2());
        log.info("{}", e.getF2());

        log.info("看看对象的类型:{}", e.getF3().getClass()); //会发现是一个CGlib的代理对象
        log.info("{}", e.getF3());
        log.info("{}", e.getF3());
        log.info("{}", e.getF3());

        log.info("看看对象的类型:{}", e.getF4().getClass()); //会发现是一个CGlib的代理对象
        log.info("{}", e.getF4());
        log.info("{}", e.getF4());
        log.info("{}", e.getF4());
        context.close();
    }
}
