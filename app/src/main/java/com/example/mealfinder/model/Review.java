package com.example.mealfinder.model;

public class Review {

    private String user;
    private String text;
    private int restaurantID;
    private float rating;

    public Review() {
    }

    public Review(int restaurantID, String user, String text) {
        this.restaurantID = restaurantID;
        this.user = user;
        this.text = text;
    }

    public Review( int restaurantID,String user, String text, float rating) {
        this.user = user;
        this.text = text;
        this.restaurantID = restaurantID;
        this.rating = rating;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
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
