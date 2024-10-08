package com.kul.a09;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class E {
    @Autowired
    @Lazy
    private F1 f1;

    @Autowired
    private F2 f2;

    @Autowired
    private ObjectFactory<F3> f3;

    @Autowired
    private ApplicationContext applicationContext;

    public F1 getF1() {
        return f1;
    }

    public F3 getF3() {
        return f3.getObject();
    }

    public F2 getF2() {
        return f2;
    }

    public F4 getF4() {
        return applicationContext.getBean(F4.class);
    }
}
