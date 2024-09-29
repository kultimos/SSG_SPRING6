package com.kul.a05;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

public class A05Application {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", Config.class);
        // 添加一个BeanFactory后置处理器,就可以发现可以识别@Bean和@ComponentScan等注解,并使其生效了
        // ConfigurationClassPostProcessor后置处理器能解析的注解有:
        // @ComponentScan、@Bean、@Import、@ImportResource
        context.registerBean(ConfigurationClassPostProcessor.class);

        // 主要就是用来扫描Mybatis的mapper接口,将之夜纳入到容器中进行管理
        context.registerBean(MapperScannerConfigurer.class, beanDefinition -> {
            beanDefinition.getPropertyValues().add("basePackage", "com.kul.a05.mapper");
        });

        context.refresh();

        for(String name: context.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        context.close();
    }
}

/**
  最初的纯净版,会发现我们最终context中只有一个bean,即我们手动添加的config,而在我们Config类中@Bean注解和@ComponentScan注解,均为生效
  未生效的原因,还是缺少了对应的后置处理器
public class A05Application {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", Config.class);

        context.refresh();

        for(String name: context.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        context.close();
    }
}
 *
 */