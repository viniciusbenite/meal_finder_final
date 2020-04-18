package com.example.mealfinder.model;

import java.util.Objects;

public class Diet {

    private String diet;


    public Diet(){

    }
    public Diet(String diet) {
        this.diet = diet;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    @Override
    public String toString() {
        return diet;

    }

}
