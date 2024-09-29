package com.kul.a05;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Bean1 {
    private static final Logger log = LoggerFactory.getLogger(com.kul.a05.Bean1.class);

    public Bean1( ) {
        log.info("我被Spring容器管理了");
    }
}
