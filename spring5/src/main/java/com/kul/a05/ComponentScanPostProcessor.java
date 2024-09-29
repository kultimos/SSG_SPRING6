package com.kul.a05;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

public class ComponentScanPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        ComponentScan componentScan = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);// 在指定类中找是否有ComponentScan注解
        if (componentScan != null) {
            String[] strings = componentScan.basePackages(); // 获取要扫描的包路径
            for(String s : strings) {
                String path = "classpath*:" + s.replace(".", "/") + "/**/*.class";
                try {
                    // 根据通配符写法,找到多个resource资源
                    Resource[] resources = new PathMatchingResourcePatternResolver().getResources(path);
                    CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory(); // 一个工厂类,用于读取类元信息
                    AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator(); // 一个工具类,用于生成bean名称
                    for(Resource resource : resources) {
                        MetadataReader reader = factory.getMetadataReader(resource);
//                    System.out.println("类名: " + reader.getClassMetadata().getClassName());
                        // 这里我们的Bean3用@Controller注解修饰,那如果我们只检索Component注解,是无法检测到Bean3的
                        // 使用hasAnnotation()和派生注解检查方法hasMetaAnnotation(),可以实现bean的无遗漏
//                    System.out.println("是否有Component注解: " + reader.getAnnotationMetadata().hasAnnotation(Component.class.getName()));
//                    System.out.println("是否有Component派生注解: " + reader.getAnnotationMetadata().hasMetaAnnotation(Component.class.getName()));
                        if(reader.getAnnotationMetadata().hasAnnotation(Component.class.getName()) ||  // 如果一个类直接或间接的有component注解,则加入bean
                                reader.getAnnotationMetadata().hasMetaAnnotation(Component.class.getName())) {
                            BeanDefinition bd = BeanDefinitionBuilder.genericBeanDefinition(reader.getClassMetadata().getClassName()).getBeanDefinition();//根据类名,生产bean实例
                            if(configurableListableBeanFactory instanceof DefaultListableBeanFactory) {
                                DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
                                String beanName = generator.generateBeanName(bd, beanFactory);
                                beanFactory.registerBeanDefinition(beanName, bd);
                            }



                        }
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        }
    }
}
