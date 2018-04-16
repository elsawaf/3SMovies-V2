package com.elsawaf.thebrilliant.a3smovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsList {
    @SerializedName("id")
    private int movieId;
    private List<MovieReview> results;

    public List<MovieReview> getResults() {
        return results;
    }
}
