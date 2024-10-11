package com.kul.a13;


import org.springframework.cglib.core.Signature;

// TargetFastClass伴随着MethodProxy.create方法的使用而创建
// 该类是在 使用 methodProxy.invoke(new Target(), objects)时生效
public class TargetFastClass {
    static Signature s0 = new Signature("save", "()V");
    static Signature s1 = new Signature("save", "(I)V");
    static Signature s2 = new Signature("save", "(J)V");


    public static void main(String[] args) throws Throwable {
        TargetFastClass targetFastClass = new TargetFastClass();
        int index = targetFastClass.getIndex(new Signature("save", "(I)V"));
        System.out.println(index);
        targetFastClass.invoke(index, new Target(), new Object[]{100});
    }

    /**
     * save0Proxy = MethodProxy.create(Target.class, $Proxy1.class, "()V", "save", "saveSuper"),
     * MethodProxy.create方法在底层做的事情就是getIndex(),create方法的一系列参数实际上就是signature
     *
     * 根据方法参数信息获取目标对象中对应方法的编号
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
            ((Target) target).save();
            return null;
        } else if(index == 1) {
            ((Target) target).save((int)args[0]);
            return null;
        } else if(index == 2) {
            ((Target) target).save((long)args[0]);
        }
        throw new RuntimeException("没有此方法");
    }
}
