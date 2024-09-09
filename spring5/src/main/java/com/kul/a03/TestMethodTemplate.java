package com.kul.a03;

import java.util.ArrayList;
import java.util.List;

public class TestMethodTemplate {
    public static void main(String[] args) {
        MyBeanFactory beanFactory = new MyBeanFactory();
        // 目前我们这个写法其实就是复刻了我们之前将bean的后置处理器加到我们beanFactory中的动作
        beanFactory.addBeanPostProcessors(object -> System.out.println("功能增强"));
        beanFactory.addBeanPostProcessors(object -> System.out.println("可以增强好多个"));
        beanFactory.getBean();
    }


    static class MyBeanFactory{
        private List<BeanPostProcessor> processors = new ArrayList<>();

        public Object getBean() {
            Object bean = new Object();
            System.out.println("构造" + bean);
            System.out.println("依赖注入" + bean);
            for(BeanPostProcessor postProcessor : processors) {
                postProcessor.inject(bean);
            }
            System.out.println("初始化" + bean);
            return bean;
        }

        public void addBeanPostProcessors(BeanPostProcessor postProcessor){
            processors.add(postProcessor);
        }
    }

    interface BeanPostProcessor{
        void inject(Object object);  // 对依赖注入阶段的扩展
    }
}
