package com.example.mealfinder.model;

import com.google.gson.annotations.SerializedName;

public class Restaurant {

    @SerializedName("restaurant")
    private RestaurantInfo restaurantInfo;

    public Restaurant(RestaurantInfo restaurantInfo) {
        this.restaurantInfo = restaurantInfo;
    }

    public RestaurantInfo getRestaurantInfo() {
        return restaurantInfo;
    }

    public void setRestaurantInfo(RestaurantInfo restaurantInfo) {
        this.restaurantInfo = restaurantInfo;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "restaurantInfo=" + restaurantInfo +
                '}';
    }
}
