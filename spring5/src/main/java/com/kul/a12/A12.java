package com.kul.a12;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class A12 {

    interface Foo{
        int foo();

        void bar();
    }

    static class Target implements Foo{

        @Override
        public int foo() {
            System.out.println("Target foo");
            return 3;
        }

        @Override
        public void bar() {
            System.out.println("Target bar");
        }
    }

    interface InvocationHandler{
        Object invoke(Object proxy, Method method, Object[] args);
    }

    public static void main(String[] args) {
        $Proxy0 proxy0 = new $Proxy0(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                try {
                    System.out.println("before前置增强");
                    return method.invoke(new Target(), args);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        proxy0.foo();
        proxy0.bar();
    }
}
