package com.kul.a13;


import org.springframework.cglib.core.Signature;

//该类是在 使用 methodProxy.invokeSuper(proxy, objects)时生效
public class ProxyFastClass {
    static Signature s0 = new Signature("saveSuper", "()V");
    static Signature s1 = new Signature("saveSuper", "(I)V");
    static Signature s2 = new Signature("saveSuper", "(J)V");


    public static void main(String[] args) throws Throwable {
        ProxyFastClass proxyFastClass = new ProxyFastClass();
        int index = proxyFastClass.getIndex(new Signature("saveSuper", "(J)V"));
        System.out.println(index);
        proxyFastClass.invoke(index, new $Proxy1(), new Object[]{29L});
    }

    /**
     * save0Proxy = MethodProxy.create(Target.class, $Proxy1.class, "()V", "save", "saveSuper"),
     * MethodProxy.create方法在底层做的事情就是getIndex(),create方法的一系列参数实际上就是signature
     *
     * 根据方法参数信息获取代理对象中对应方法的编号
     * @param signature 该参数中包含了方法名字、参数返回值信息
     * @return 该方法最终返回一个方法的编号
     */
    public int getIndex(Signature signature) {
        if(s0.equals(signature)) {
            return 0;
        } else if(s1.equals(signature)) {
            return 1;
        } else if(s2.equals(signature)) {
            return 2;
        }
        return -1;
    }


    /**
     * 根据方法编号,正常调用目标对象方法
     * @param index 方法编号
     * @param target 目标对象
     * @param args 参数列表
     * @return
     */
    public Object invoke(int index, Object target, Object[] args) throws Throwable {
        if(index == 0) {
            (($Proxy1) target).saveSuper();
            return null;
        } else if(index == 1) {
            (($Proxy1)  target).saveSuper((int)args[0]);
            return null;
        } else if(index == 2) {
            (($Proxy1)  target).saveSuper((long)args[0]);
            return null;
        }
        throw new RuntimeException("没有此方法");
    }
}
