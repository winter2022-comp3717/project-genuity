package com.bcit.project_genuity;

public class Event {
    /**
     * String - Name
     * String - Host
     * String - DateTime
     * String - ImageURL
     * String - Description
     * String - Location
     */

    public String name;
    public String host;
    public String datetime;
    public String imgUrl;
    public String description;
    public String location;

    public Event() {

    }

    public Event(String name, String host, String datetime, String imgUrl, String description, String location) {
        this.name = name;
        this.host = host;
        this.datetime = datetime;
        this.imgUrl = imgUrl;
        this.description = description;
        this.location = location;
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
}
