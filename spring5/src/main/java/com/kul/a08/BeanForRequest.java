package com.kul.a08;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Slf4j
@Scope("request")
@Component
public class BeanForRequest {

    @PreDestroy
    public void destroy() {
        log.info("destroy");
    }
}
