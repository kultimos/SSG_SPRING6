package com.kul.a04;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.StandardEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// AutowiredAnnotationBeanPostProcessor 运行分析
public class DigInAutowired {
    public static void main(String[] args) throws Throwable {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton("bean2", new Bean2());
        beanFactory.registerSingleton("bean3", new Bean3());
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders); //增加${}的解析器

        //
        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setBeanFactory(beanFactory);

        Bean1 bean1 = new Bean1();
        System.out.println(bean1);
//        processor.postProcessProperties(null, bean1, "bean1"); //执行依赖注入 @Autowired @Value
//        // 结果中bean3是null,因为bean3是通过@Resource注解注入的,所以这里为null是正确的
//        System.out.println(bean1);

        //接下来通过反射来看一下真正执行@Autowired的代码
        Method findAutowiringMetadata = AutowiredAnnotationBeanPostProcessor.class.getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        findAutowiringMetadata.setAccessible(true);
        // 获取需要进行@Autowired的信息
        InjectionMetadata metaData = (InjectionMetadata) findAutowiringMetadata.invoke(processor, "bean1", Bean1.class, null);//获取bean1上添加了@Autowired和@Value的成员变量、方法参数信息
        // 执行注入
        metaData.inject(bean1, "bean1", null);
        System.out.println(bean1);
        System.out.println("-------------------");
        // @Autowired修饰的属性是如何实现自动注入的
        Field bean3 = Bean1.class.getDeclaredField("bean3");
        Class<?> type = bean3.getType();
        System.out.println(type);
        // 这里的true表示,后续依赖注入时如果找不到对应类型的值,则会报错;若是false则不会
        DependencyDescriptor dd1 = new DependencyDescriptor(bean3, false);
        // 会根据我们传入的dd1里面bean3对应的类型,去容器中找bean3这个类型的对象(因为@Autowired默认就是按类型查找),后续就可以通过反射实现把bean3的对象注入到bean1的成员变量中
        Object o1 = beanFactory.doResolveDependency(dd1, null, null, null);
        System.out.println(o1);

        //@Autowired修饰的方法是如何实现自动注入的
        Method setHome = Bean1.class.getDeclaredMethod("setHome", String.class);
        DependencyDescriptor dd2 = new DependencyDescriptor(new MethodParameter(setHome, 0), false);
        Object o2 = beanFactory.doResolveDependency(dd2, null, null, null);
        System.out.println(o2);
    }
}
