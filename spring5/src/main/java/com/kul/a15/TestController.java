package com.kul.a15;

import com.kul.a15.aop.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private MyService myService;

    @GetMapping("/execution")
    public void test1() {
        myService.perform();
    }
}
