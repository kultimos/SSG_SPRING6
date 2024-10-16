package com.kul.a15.aop;

import org.springframework.stereotype.Service;

@Service
public class MyService  {
    public void perform() {
        System.out.println("执行业务逻辑");
    }
}
