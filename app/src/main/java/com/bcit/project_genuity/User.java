package com.bcit.project_genuity;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String id;
    public String name;
    public String email;
    public String phone;
    public ArrayList<String> events;

    public User() {

    }

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.events = new ArrayList<>();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void addEvent(String event) {
        this.events.add(event);
    }

    public ArrayList<String> getEvents() {
        return this.events;
    }
}
