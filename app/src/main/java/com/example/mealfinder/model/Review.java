package com.example.mealfinder.model;

import java.util.HashMap;
import java.util.List;

public class Review {

    private String user;
    private String text;
    private int restaurantID;

    public Review() {
    }

    public Review(int restaurantID, String user, String text) {
        this.restaurantID = restaurantID;
        this.user = user;
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    @Override
    public String toString() {
        return "Review{" +
                "user='" + user + '\'' +
                ", text='" + text + '\'' +
                ", restaurantID=" + restaurantID +
                '}';
    }
}
