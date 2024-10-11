package com.kul.a11;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxyDemo {

    static class Target {
        public void foo() {
            System.out.println("target foo");
        }
    }

    public static void main(String[] args) {
        Target target = new Target();
        Target proxy = (Target)Enhancer.create(Target.class, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("before");
                // 这三种写法都可以,但是使用methodProxy的两种方式内部没有使用反射
                Object result1 = method.invoke(target, args);
                Object result2 = methodProxy.invoke(target, args); //通过目标对象
                Object result3 = methodProxy.invokeSuper(o, objects); //通过代理对象
                System.out.println("after");
                return result1;
            }
        });
        proxy.foo();
    }
}
