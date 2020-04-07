package com.example.mealfinder.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantList {

    @SerializedName("restaurants")
    private List<Restaurant> restaurantList;

    public RestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }
}
