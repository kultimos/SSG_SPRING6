package com.kul.a12;


import com.kul.a09.E;

import javax.el.MethodNotFoundException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

public class $Proxy0 implements A12.Foo {

    private A12.InvocationHandler invocationHandler;


    public $Proxy0(A12.InvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    @Override
    public int foo() {
        // 理想状态下,在代理对象的foo方法中,要做两件事
        // 1.进行功能增强方法的调用
        // 2.调用真实对象的foo方法
//        System.out.println("before前置增强");
//        A12.Target target = new A12.Target();
//        target.foo();

        // 但是在真实场景中,代理对象要干什么是不确定的,所以在代理对象foo方法中的逻辑也是不确定的
        // 所以真实的情况是,代理对象并不会把逻辑写在继承的接口的重写方法中
        // 而是将代理对象要做的逻辑定义为一个抽象方法,然后具体你要用这个代理类做什么时,再讲这个抽象方法的实现提供,就可以完成代理的实现
        // 那么这个定义抽象方法和提供抽象方法的实现,在JDK动态代理中,就使用invocationHandler实现的
        try {
            return (int)invocationHandler.invoke(this, foo, new Object[0]);
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    @Override
    public void bar() {
        try {
            invocationHandler.invoke(this, bar, new Object[0]);
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    private static Method foo;
    private static Method bar;

    static {
        try {
            foo = A12.Foo.class.getMethod("foo");
            bar = A12.Foo.class.getMethod("bar");
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        }
    }
}
