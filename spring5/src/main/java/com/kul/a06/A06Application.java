package com.kul.a06;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

public class A06Application {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
//        context.registerBean("myBean", MyBean.class);
        context.registerBean("myConfig2", MyConfig2.class);
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);
        context.registerBean(CommonAnnotationBeanPostProcessor.class);
        context.registerBean(ConfigurationClassPostProcessor.class);


        /**
         * 这里简单说下refresh方法的执行顺序
         * 1.在容器中找到所有的BeanFactory的后处理器来执行
         * 2.添加Bean的后处理器
         * 3.初始化单例
         */
        context.refresh();

        context.close();
    }
}
