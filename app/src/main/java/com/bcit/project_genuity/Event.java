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
    private ArrayList<User> users;

    public Event() {

    }

    public Event(String id, String name, String host, String datetime, String imgUrl, String description, String location) {
        this.id = id;
        this.name = name;
        this.host = host;
        this.datetime = datetime;
        this.imgUrl = imgUrl;
        this.description = description;
        this.location = location;
        this.users = new ArrayList<>();
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

    public void addUser(User user) {
        this.users.add(user);
    }
}
