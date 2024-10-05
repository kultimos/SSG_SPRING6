package com.kul.a05;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class A05Application {
    public static void main(String[] args) throws IOException {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", Config.class);

        /**
         * 模拟ComponentScan注解的解析
         */
//        ComponentScan componentScan = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);// 在指定类中找是否有ComponentScan注解
//        if (componentScan != null) {
//            String[] strings = componentScan.basePackages(); // 获取要扫描的包路径
//            for(String s : strings) {
//                String path = "classpath*:" + s.replace(".", "/") + "/**/*.class";
//                Resource[] resources = context.getResources(path);
//                CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory(); // 一个工厂类,用于读取类元信息
//                AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator(); // 一个工具类,用于生成bean名称
//                for(Resource resource : resources) {
//                    MetadataReader reader = factory.getMetadataReader(resource);
////                    System.out.println("类名: " + reader.getClassMetadata().getClassName());
//                    // 这里我们的Bean3用@Controller注解修饰,那如果我们只检索Component注解,是无法检测到Bean3的
//                    // 使用hasAnnotation()和派生注解检查方法hasMetaAnnotation(),可以实现bean的无遗漏
////                    System.out.println("是否有Component注解: " + reader.getAnnotationMetadata().hasAnnotation(Component.class.getName()));
////                    System.out.println("是否有Component派生注解: " + reader.getAnnotationMetadata().hasMetaAnnotation(Component.class.getName()));
//                    if(reader.getAnnotationMetadata().hasAnnotation(Component.class.getName()) ||  // 如果一个类直接或间接的有component注解,则加入bean
//                            reader.getAnnotationMetadata().hasMetaAnnotation(Component.class.getName())) {
//                        BeanDefinition bd = BeanDefinitionBuilder.genericBeanDefinition(reader.getClassMetadata().getClassName()).getBeanDefinition();//根据类名,生产bean实例
//                        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory(); // 获取当前的BeanFactory
//                        String beanName = generator.generateBeanName(bd, beanFactory);
//                        beanFactory.registerBeanDefinition(beanName, bd);
//
//
//                    }
//                }
//
//            }
//        }

//        /**
//         * 接下来这段是关于 通过后处理器实现@Bean注解的代码
//         */
//        CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
//        MetadataReader reader = factory.getMetadataReader(new ClassPathResource("com/kul/a05/Config.class"));
//        Set<MethodMetadata> methods = reader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName()); //获取所有被@Bean标注的方法的信息
//        for(MethodMetadata method: methods) {
//            System.out.println(method);
//            //属性解析: 可以获取该方法的Bean注解的属性信息,返回值是一个Map.key是属性名,value是属性值
//            Map<String, Object> annotationAttributes = method.getAnnotationAttributes(Bean.class.getName());
//            for (Map.Entry<String, Object> stringObjectEntry : annotationAttributes.entrySet()) {
//                System.out.println(stringObjectEntry.getKey() + " : " + stringObjectEntry.getValue());
//            }
//            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
//            // 这里需要理解,@Configuration修饰的类,本质上相当于一个工厂对象;我们在config中定义的多个@Bean修饰的方法,实际上是一系列工厂方法
//            builder.setFactoryMethodOnBean(method.getMethodName(), "config");
//            builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR); //若不手动设置自动注入类型,则遇见有参数的工厂方法会跳过
//            AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
//            context.getDefaultListableBeanFactory().registerBeanDefinition(method.getMethodName(), beanDefinition);
//        }

        // 因为我们将后置处理器已经封装,所以这里我们只需要直接注册该后置处理器即可
        context.registerBean(ComponentScanPostProcessor.class);
        context.registerBean(AtBeanPostProcessor.class);
        context.refresh();

        for(String name: context.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        context.close();
    }
}

/**
 * 配置了BeanFactory后置处理器的版本,
 * 已经可以成功扫描到多个bean
 * public static void main(String[] args) {
 *         GenericApplicationContext context = new GenericApplicationContext();
 *         context.registerBean("config", Config.class);
 *         // 添加一个BeanFactory后置处理器,就可以发现可以识别@Bean和@ComponentScan等注解,并使其生效了
 *         // ConfigurationClassPostProcessor后置处理器能解析的注解有:
 *         // @ComponentScan、@Bean、@Import、@ImportResource
 *         context.registerBean(ConfigurationClassPostProcessor.class);
 *
 *         // 主要就是用来扫描Mybatis的mapper接口,将之夜纳入到容器中进行管理
 *         context.registerBean(MapperScannerConfigurer.class, beanDefinition -> {
 *             beanDefinition.getPropertyValues().add("basePackage", "com.kul.a05.mapper");
 *         });
 *
 *         context.refresh();
 *
 *         for(String name: context.getBeanDefinitionNames()) {
 *             System.out.println(name);
 *         }
 *
 *         context.close();
 *     }
 */


/**
 * 最初的纯净版,会发现我们最终context中只有一个bean,即我们手动添加的config,而在我们Config类中@Bean注解和@ComponentScan注解,均未生效
 * 未生效的原因,还是缺少了对应的后置处理器
 * public class A05Application {
 *     public static void main(String[] args) {
 *         GenericApplicationContext context = new GenericApplicationContext();
 *         context.registerBean("config", Config.class);
 *
 *         context.refresh();
 *
 *         for(String name: context.getBeanDefinitionNames()) {
 *             System.out.println(name);
 *         }
 *
 *         context.close();
 *     }
 * }
 */
