package com.example.mealfinder.model;

import com.google.gson.annotations.SerializedName;

public class UserRating {

    @SerializedName("aggregate_rating")
    private String aggregateRating;

    @SerializedName("votes")
    private String votes;

    public UserRating(){

    }

    public UserRating(String aggregateRating, String votes) {
        this.aggregateRating = aggregateRating;
        this.votes = votes;
    }

    public String getAggregateRating() {
        return aggregateRating;
    }

    public void setAggregateRating(String aggregateRating) {
        this.aggregateRating = aggregateRating;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }
}
