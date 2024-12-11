package com.example.campusexpensemanager;

public class User {
    private String email;
    private String name;
    private int age;
    private String phone;

    public User(String email, String name, int age, String phone) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.phone = phone;
    }

    // Getter và Setter
    public String getEmail() { return email; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getPhone() { return phone; }
}

