package com.bcit.project_genuity;

import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Serializable {
    /**
     * String - Name
     * String - Host
     * String - DateTime
     * String - ImageURL
     * String - Description
     * String - Location
     */

    public String id;
    public String name;
    public String host;
    public String datetime;
    public String imgUrl;
    public String description;
    public String location;
    public ArrayList<User> users;
    public Long capacity;
    public int registeredUsers;

    public Event() {

    }

    public Event(String id, String name, String host, String datetime, String imgUrl, String description, String location, Long capacity) {
        this.id = id;
        this.name = name;
        this.host = host;
        this.datetime = datetime;
        this.imgUrl = imgUrl;
        this.description = description;
        this.location = location;
        this.capacity = capacity;
        this.users = new ArrayList<>();
    }

    public Event(String id, String name, String host, String datetime, String imgUrl, String description, String location, Long capacity, int registeredUsers) {
        this.id = id;
        this.name = name;
        this.host = host;
        this.datetime = datetime;
        this.imgUrl = imgUrl;
        this.description = description;
        this.location = location;
        this.capacity = capacity;
        this.users = new ArrayList<>();
        this.registeredUsers = registeredUsers;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public ArrayList<User> getUsers () {return this.users;}

    public int getNumberOfUsers () {return this.registeredUsers;}

    public Long getCapacity () {return this.capacity;}

    public void addUser(User user) {
        this.users.add(user);
    }
}
