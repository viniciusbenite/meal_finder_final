package com.example.mealfinder.model;

import com.google.gson.annotations.SerializedName;

public class RestaurantInfo {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("location")
    private Location location;

    @SerializedName("thumb")
    private String thumb;

    public RestaurantInfo(String id, String name, Location location, String thumb) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.thumb = thumb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return "RestaurantInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
