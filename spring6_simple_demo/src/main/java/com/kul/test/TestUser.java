package com.kul.test;

import com.kul.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TestUser {

    @Test
    public void testUser() {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        User user = (User) context.getBean("user");
        System.out.println(user);
        user.method();
    }

    @Test
    public void testUser2() throws Exception {
        Class clazz = Class.forName("com.kul.model.User");
//        Object o = clazz.newInstance(); 该方法在JDK17中已经过时
        User user = (User)clazz.getDeclaredConstructor().newInstance();
        user.method();
    }
}
