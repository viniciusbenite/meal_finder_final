package com.example.mealfinder.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Diet diet1 = (Diet) o;
        return Objects.equals(diet, diet1.diet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diet);
    }
}
