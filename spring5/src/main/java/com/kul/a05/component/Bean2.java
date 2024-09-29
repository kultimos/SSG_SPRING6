package com.kul.a05.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Bean2 {
    private static final Logger log = LoggerFactory.getLogger(Bean2.class);

    public Bean2( ) {
        log.info("我被Spring容器管理了");
    }
}
