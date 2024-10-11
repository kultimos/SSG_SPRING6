package com.kul.a11;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxyDemo {

    interface Foo{
        void foo();
    }

    static final class Target implements Foo { //final也没关系,只要这个类实现了接口,就可以用jdk进行代理
        public void foo() {
            System.out.println("target foo");
        }
    }

    public static void main(String[] args) {

        ClassLoader loader = JdkProxyDemo.class.getClassLoader(); //ClassLoader是用来加载在运行期间动态生成的代理类的字节码文件的
        Foo proxy = (Foo) Proxy.newProxyInstance(loader, new Class[]{Foo.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object p, Method method, Object[] args) throws Throwable {
                System.out.println("before...");
                method.invoke(p, args);
                return null;
            }
        });
        proxy.foo();
    }
}
