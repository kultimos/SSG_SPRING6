package com.kul.a18;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class A18_2 {

    static class Target{
        public void foo() {
            System.out.println("Target.foo()");
        }
    }

    static class Advice1 implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("Advice1.before()");
            Object result = invocation.proceed();
            System.out.println("Advice1.after()");
            return result;
        }
    }

    static class Advice2 implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("Advice2.before()");
            Object result = invocation.proceed();
            System.out.println("Advice2.after()");
            return result;
        }
    }

    static class MyInvocation implements MethodInvocation {
        private Object target;
        private Method method;
        private Object[] args;
        private List<MethodInterceptor> methodInterceptorList;
        private Integer count = 1; // 该参数用于控制递归的调用次数

        public MyInvocation(Object target, Object[] args, Method method, List<MethodInterceptor> methodInterceptorList) {
            this.target = target;
            this.args = args;
            this.method = method;
            this.methodInterceptorList = methodInterceptorList;
        }

        @Override
        public Method getMethod() {
            return method;
        }

        @Override
        public Object[] getArguments() {
            return args;
        }

        /**
         * proceed:核心方法
         * 用于触发环绕通知,并监控环绕通知,直接所有环绕通知执行结束,再执行目标方法;
         *
         * 代码很简单,看起来也没用递归,但之所以能形成递归,是因为,我们从list中取出的一个个advice,它本身实现了MethodInterceptor接口,
         * 在通知(advice)的内部,如下面的代码,也调用了proceed()方法,是这样形成的递归,我们proceed方法其实核心做的是就是依次执行所有的advice,
         * 增强逻辑是在各个advice中自己写的;
         * static class Advice1 implements MethodInterceptor {
         *     public Object invoke(MethodInvocation invocation) throws Throwable {
         *         System.out.println("advice1 before");
         *         Object result = invocation.proceed();
         *         System.out.println("advice1 after");
         *         return result;
         *     }
         * }
         * 并且从这端代码中,也可以洞悉,为什么我们之前执行多个切面方法时,它们的顺序是
         *  - advice1_beofre
         *  - advice2_before
         *  - target_method
         *  - advice2_after
         *  - advice1_after
         * 这是因为我们从list中先取出advice1,然后调用其proceed方法,进去advice1自己的invoke方法,然后进行增强,增强后调用invocation.proceed()方法
         * 又进入调用链中,取出list中第二个advice2,在调用advice2的proceed方法,执行完以后又调用invocation.proceed(),而再次进入调用链发现,已经没有
         * 其他的切面了,就会执行目标方法method.invoke(target, args),目标方法执行结束以后把结果返回,根据递归逻辑,正好就是到了advice2中,进而再执行
         * advice2的after方法,所以最终的增强的结果类似套娃;
         * @return
         * @throws Throwable
         */
        @Override
        public Object proceed() throws Throwable {
            if(count > methodInterceptorList.size()) {
                return method.invoke(target, args);
            }
            MethodInterceptor methodInterceptor = methodInterceptorList.get(count++ - 1);
            return methodInterceptor.invoke(this);
        }

        @Override
        public Object getThis() {
            return target;
        }

        @Override
        public AccessibleObject getStaticPart() {
            return method;
        }
    }

    public static void main(String[] args) throws Throwable {
        Target target = new Target();
        List<MethodInterceptor> adviceList = new ArrayList<>();
        adviceList.add(new Advice1());
        adviceList.add(new Advice2());
        MyInvocation myInvocation = new MyInvocation(target, new Object[0], Target.class.getMethod("foo"), adviceList);
        Object proceed = myInvocation.proceed();
    }
}
