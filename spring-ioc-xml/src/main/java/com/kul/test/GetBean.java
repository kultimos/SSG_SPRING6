package com.kul.test;

import com.kul.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GetBean {

    @Test
    public void testAll() throws ClassNotFoundException {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        getBeanByClassType(context);
        getBeanById(context);
        getBeanByClassType(context);
    }


    public void getBeanById(ApplicationContext context){
        User bean = (User)context.getBean("user");
        bean.run();
        System.out.println(bean);
    }

    public void getBeanByClassType(ApplicationContext context) throws ClassNotFoundException {
        Class clazz = Class.forName("com.kul.model.User");
        User bean1 = context.getBean(User.class);
        User bean2 = (User) context.getBean(clazz);
        System.out.println(bean1);
        System.out.println(bean2);
        bean1.run();
        bean2.run();
    }


    public void getBeanByTypeAndId(ApplicationContext context) {
        User bean = context.getBean("user", User.class);
        bean.run();
    }
}
