package com.kul.test;

import com.kul.model.UserDao;
import com.kul.model.UserDaoImplOne;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class InterFaceBean {


    /**
     * <bean id="userImplOne" class="com.kul.model.UserDaoImplOne"></bean>
     * <bean id="userImplTwo" class="com.kul.model.UserDaoImplTwo"></bean>
     * 如果xml中只配置一个可以通过接口的class对象获取实现类的实例,但如果xml中有多个实现类的bean信息,则再使用这种方法会报错;
     */
    @Test
    public void test() {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        UserDaoImplOne bean = (UserDaoImplOne) context.getBean(UserDao.class);
        bean.test();
    }
}
