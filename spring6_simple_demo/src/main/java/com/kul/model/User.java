package com.kul.model;

public class User {
    private String name;

    public User() {
        System.out.println("无参构造");
    }

    public User(String name) {
        this.name = name;
    }
    public void method(){
        System.out.println("我的名字是:" + name);
    }
}
