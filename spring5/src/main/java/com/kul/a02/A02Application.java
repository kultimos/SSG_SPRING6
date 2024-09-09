package com.kul.a02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebApplicationContext;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.Controller;


@SpringBootApplication
public class A02Application {

    private static final Logger log = LoggerFactory.getLogger(A02Application.class);

    public static void main(String []args){
//        SpringApplication.run(A02Application.class, args);

        // 其实ClassPathXmlApplicationContext本质上也是通过创建一个beanFactory,然后通过对配置文件中信息的映射最终将Bean的定义信息写入到BeanFactory中;
//        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//        System.out.println("读取之前");
//        for(String name : beanFactory.getBeanDefinitionNames()) {
//            System.out.println(name);
//        }
//        System.out.println("读取之后");
//        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
//        reader.loadBeanDefinitions(new ClassPathResource("B01.xml"));
//        for(String name : beanFactory.getBeanDefinitionNames()) {
//            System.out.println(name);
//        }

        testClassPathXmlApplicationContext();
//        testFileSystemXmlApplicationContext();
//        testAnnotationConfigApplicationContext();
        testAnnotationConfigServletWebServerApplicationContext();

    }

    /**
     * 较为经典的容器,基于classpath下xml格式的配置文件来创建
     */
    public static void testClassPathXmlApplicationContext() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("B01.xml");
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for(String u : beanDefinitionNames) {
            System.out.println(u);
        }
        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    /**
     * 基于磁盘路径下xml格式的配置文件来创建
     */
    public static void testFileSystemXmlApplicationContext() {
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("D:\\kultimos\\THE_SPRING\\spring5\\src\\main\\resources\\B01.xml");
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for(String u : beanDefinitionNames) {
            System.out.println(u);
        }
    }

    /**
     * 较为经典的容器,基于java配置类来创建
     * 从这个打印的结果中就可以看出,applicationContext自动加上了很多beanFactory需要手动去处理的beanFactory后置处理器和bean的后置处理器
     */
    public static void testAnnotationConfigApplicationContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        for(String u : context.getBeanDefinitionNames()) {
            System.out.println(u);
        }
        System.out.println(context.getBean(Bean2.class).getBean1());
    }


    /**
     * 较为经典的容器,基于java配置类来创建,用于web环境
     * 感觉这一部分实际上就是我们平时写Controller的底层逻辑,通过内嵌的tomcat实现了对springweb程序的处理
     */
    public static void testAnnotationConfigServletWebServerApplicationContext() {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);

    }

    @Configuration
    static class WebConfig{
        // 内嵌容器
        @Bean
        public ServletWebServerFactory servletWebServerFactory() {
            return new TomcatServletWebServerFactory();
        }

        // 事件分发器
        @Bean
        public DispatcherServlet dispatcherServlet() {
            return new DispatcherServlet();
        }

        // 将事件分发器注册进内嵌的tomcat容器
        @Bean
        public DispatcherServletRegistrationBean registrationBean(DispatcherServlet dispatcherServlet) {
            return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        }

        @Bean("/hello")
        public Controller Controller1(){
            return (request, response) -> {
                response.getWriter().print("i love in");
                return null;
            };
        }

    }





    @Configuration
    static class Config{
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2(Bean1 bean1) {
            Bean2 bean2 = new Bean2();
            bean2.setBean1(bean1);
            return bean2;
        }


    }

    static class Bean1{

    }

    static class Bean2{
        private Bean1 bean1;

        public void setBean1(Bean1 bean1) {
            this.bean1 = bean1;
        }

        public Bean1 getBean1() {
            return bean1;
        }
    }

}
