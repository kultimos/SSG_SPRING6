package com.kul.model;

import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Data
public class User {

    public User(String name, int age, Role role, String[] permission, List<Role> fuck, Map<String, Role> nba) {
        System.out.println("有参构造");
        this.name = name;
        this.age = age;
        this.role = role;
        this.permission = permission;
        this.fuck = fuck;
        this.nba = nba;
    }

    private String name;
    private int age;
    private Role role;
    private String[] permission;
    private List<Role> fuck;
    private Map<String, Role> nba;

    public User() {
        System.out.println("无参构造");
    }

    public void run () {
        System.out.println("run ..............." + role.getName());
    }


}
