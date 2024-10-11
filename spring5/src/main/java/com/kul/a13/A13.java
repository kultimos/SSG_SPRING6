package com.kul.a13;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class A13 {
    public static void main(String[] args) throws Throwable {
        $Proxy1 proxy1 = new $Proxy1();
        proxy1.setMethodInterceptor(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("进行一个功能的增强");
                method.invoke(new Target(), objects);  // 内部使用反射
                methodProxy.invoke(new Target(), objects); //内部没有使用反射
                methodProxy.invokeSuper(o, objects); //内部没有使用反射
                return null;
            }
        });
        proxy1.save();
        proxy1.save(2);
    }
}
