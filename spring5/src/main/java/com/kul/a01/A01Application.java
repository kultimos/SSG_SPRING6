package com.kul.a01;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
public class A01Application {

    public static void main(String []args) throws NoSuchFieldException, IllegalAccessException, IOException {
        ConfigurableApplicationContext context = SpringApplication.run(A01Application.class, args);

        // 通过DefaultSingletonBeanRegistry的class 反射获取一个属性也是一个字段信息
        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        // 私有属性,需要爆破
        singletonObjects.setAccessible(true);
        // 获取对象
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        // 属性.get(对象)获取对象的属性值
        Map<String, Object> map = (Map<String, Object>) singletonObjects.get(beanFactory);
        // 拿到单例集合,进行过滤
        Map<String, Object> filteredMap = map.entrySet().stream()
                .filter(k -> k.getKey().startsWith("component"))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // 键映射函数
                        Map.Entry::getValue // 值映射函数
                ));
        filteredMap.forEach((k,v) -> {
            System.out.print("k = " + k);
            System.out.println(", v = " + v);
        });

        //-----------------
        // MessageSource, 国际化能力
        System.out.println(context.getMessage("hi", null, Locale.CHINA));

        // ResourcePatternResolver
        // classpath* 表示检查类路径下的所有内容,包括jar包;如果没有*的话,就只会找类路径下的基本文件
        Resource[] resources = context.getResources("classpath*:META-INF/spring.factories");
        for(Resource r : resources) {
            System.out.println(r);
        }

        // EnvironmentCapable 获取环境变量里的值
        String property = context.getEnvironment().getProperty("server.port");
        System.out.println(property);

        // ApplicationEventPublisher发布事件
        context.publishEvent(new UserRegisteredEvent(context));
        Component1 bean = context.getBean(Component1.class);
        bean.register();
    }
}
