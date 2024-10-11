package com.kul.a13;

public class Target {
    public void save() throws Throwable {
        System.out.println("save");
    }

    public void save(int i) throws Throwable {
        System.out.println("save(int)");
    }

    public void save(long i) throws Throwable {
        System.out.println("save(long)");
    }
}
