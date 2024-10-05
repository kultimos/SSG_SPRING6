package com.kul.a05;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class AtBeanPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        try {
            CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
            MetadataReader reader = factory.getMetadataReader(new ClassPathResource("com/kul/a05/Config.class"));
            AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata(); //获取所有跟注解相关的元数据信息
            Set<MethodMetadata> methods = reader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName()); //获取所有被@Bean标注的方法的信息
            for (MethodMetadata method : methods) {
                System.out.println(method);
                //属性解析: 可以获取该方法的Bean注解的属性信息,返回值是一个Map.key是属性名,value是属性值
                Map<String, Object> annotationAttributes = method.getAnnotationAttributes(Bean.class.getName());
//                for (Map.Entry<String, Object> stringObjectEntry : annotationAttributes.entrySet()) {
//                    System.out.println(stringObjectEntry.getKey() + " : " + stringObjectEntry.getValue());
//                }
                if(annotationAttributes.containsKey("initMethod")) {
                    System.out.println("initMethod:" + annotationAttributes.get("initMethod"));
                }
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
                // 这里需要理解,@Configuration修饰的类,本质上相当于一个工厂对象;我们在config中定义的多个@Bean修饰的方法,实际上是一系列工厂方法
                builder.setFactoryMethodOnBean(method.getMethodName(), "config");
                builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR); //若不手动设置自动注入类型,则遇见有参数的工厂方法会跳过
                AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
                if(configurableListableBeanFactory instanceof DefaultListableBeanFactory) {
                    DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
                    beanFactory.registerBeanDefinition(method.getMethodName(), beanDefinition);
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
