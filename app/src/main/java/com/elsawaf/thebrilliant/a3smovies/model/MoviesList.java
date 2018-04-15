package com.elsawaf.thebrilliant.a3smovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by The Brilliant on 15/03/2018.
 */

public class MoviesList {
    @SerializedName("total_results")
    private int totalResults;

    List<Movie> results;

    public MoviesList(int totalResults, List<Movie> results) {
        this.totalResults = totalResults;
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<Movie> getResults() {
        return results;
    }

}
