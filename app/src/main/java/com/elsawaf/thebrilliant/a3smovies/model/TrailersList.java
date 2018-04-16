package com.elsawaf.thebrilliant.a3smovies.model;

import java.util.List;

public class TrailersList {
    private int movieId;
    private List<MovieTrailer> results;

    public TrailersList(int movieId, List<MovieTrailer> results) {
        this.movieId = movieId;
        this.results = results;
    }

    public List<MovieTrailer> getResults() {
        return results;
    }
}
