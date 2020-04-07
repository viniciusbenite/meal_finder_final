package com.example.mealfinder.model;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("locality_verbose")
    private String locality;

    public Location(String locality) {
        this.locality = locality;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    @Override
    public String toString() {
        return "Location{" +
                "locality='" + locality + '\'' +
                '}';
    }
}
