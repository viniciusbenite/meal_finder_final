package com.example.mealfinder.model;

public class FoodLog {

    private String logName;

    private String picture;

    private String mealName;

    private String date;

    public FoodLog(){

    }
    public FoodLog(String logName, String picture, String mealName, String date) {
        this.logName = logName;
        this.picture = picture;
        this.mealName = mealName;
        this.date = date;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
