package com.example.mealfinder.model;

public class Diet {

    private String diet;

    public Diet(String diet) {
        this.diet = diet;
    }
    public Diet(){
        
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
