package com.bcit.project_genuity;

public class User {
    public String name;
    public String email;
    public String phone;
    public Event[] events;

    public User() {

    }

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
