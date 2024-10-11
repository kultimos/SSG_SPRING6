package com.kul.a13;



import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.omg.CORBA.Object;

import java.lang.reflect.Method;

public class $Proxy1 extends Target{

    private MethodInterceptor methodInterceptor;

    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    static Method save0;
    static Method save1;
    static Method save2;
    static MethodProxy save0Proxy0;
    static MethodProxy save0Proxy1;
    static MethodProxy save0Proxy2;

    public void saveSuper() throws Throwable {
        super.save();
    }
    public void saveSuper(int i) throws Throwable {
        super.save(i);
    }
    public void saveSuper(long j) throws Throwable {
        super.save(j);
    }

    public void save() throws Throwable {
        methodInterceptor.intercept(this, save0, new Object[0], save0Proxy0);
    }

    public void save(int i) throws Throwable {
        methodInterceptor.intercept(this, save1, new Integer[]{i}, save0Proxy1);
    }

    public void save(long i) throws Throwable {
        methodInterceptor.intercept(this, save2, new Long[]{i}, save0Proxy2);
    }

    static {
        try {
            save0 = Target.class.getMethod("save");
            save1 = Target.class.getMethod("save", int.class);
            save2 = Target.class.getMethod("save", long.class);
            save0Proxy0 = MethodProxy.create(Target.class, $Proxy1.class, "()V", "save", "saveSuper");
            save0Proxy1 = MethodProxy.create(Target.class, $Proxy1.class, "(I)V", "save", "saveSuper");
            save0Proxy2 = MethodProxy.create(Target.class, $Proxy1.class, "(J)V", "save", "saveSuper");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
