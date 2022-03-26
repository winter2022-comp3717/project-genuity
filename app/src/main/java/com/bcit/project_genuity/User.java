package com.bcit.project_genuity;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String name;
    public String email;
    public String phone;
    public List<Event> events;

    public User() {

    }

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.events = new ArrayList<>();
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }
}
