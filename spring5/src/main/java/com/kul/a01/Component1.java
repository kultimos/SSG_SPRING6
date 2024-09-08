package com.kul.a01;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController("/register")
public class Component1 {
    private static final Logger log = LoggerFactory.getLogger(Component1.class);

    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * 名字随便起,识别的是: 1注解 2参数类型
     * @param event
     */
    @EventListener
    public void suiyi(UserRegisteredEvent event) {
        log.info("看看收到的event: {}",event);
    }

    @PostMapping
    public void register(){
        log.info("当用户注册成功之后,我要发送事件");
        publisher.publishEvent(new UserRegisteredEvent(this));
    }

}
