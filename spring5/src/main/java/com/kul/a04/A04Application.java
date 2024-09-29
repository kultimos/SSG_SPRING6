package com.kul.a04;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

public class A04Application {
    public static void main(String[] args) {
        // GenericApplicationContext是一个【干净】的容器,不会自动给我们加处理器
        GenericApplicationContext context = new GenericApplicationContext();

        // 原始方法注册三个bean
        context.registerBean("bean1", Bean1.class);
        context.registerBean("bean2", Bean2.class);
        context.registerBean("bean3", Bean3.class);

        //首次执行代码后发现Bean1中的多个注解的打印信息都没有打印,这是因为我们没有添加解析相应注解的bean后置处理器

        // 增加一个解析器,如果没有的话,会导致注入的属性字段无法被正常解析,这个解析器是辅助下面的AutowiredAnnotationBeanPostProcessor来实现@Value属性获取的;
        context.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class); // 解析 @Autowired和@Value
        context.registerBean(CommonAnnotationBeanPostProcessor.class); // 解析 @Resource、@PostConstruct、@PreDestroy



        // 初始化容器
        context.refresh(); // 执行beanFactory后处理器,添加bean后处理器,初始化所有单例

        // 销毁容器
        context.close();;
    }
}
