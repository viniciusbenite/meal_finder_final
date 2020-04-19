package com.example.mealfinder.model;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("locality")
    private String locality;

    @SerializedName("address")
    private String address;

    public Location(){

    }
    public Location(String locality, String address) {
        this.locality = locality;
        this.address=address;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Location{" +
                "locality='" + locality + '\'' +
                '}';
    }
}
