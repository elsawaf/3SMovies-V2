package com.elsawaf.thebrilliant.a3smovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersList {
    @SerializedName("id")
    private int movieId;
    private List<MovieTrailer> results;

    public List<MovieTrailer> getResults() {
        return results;
    }
}
