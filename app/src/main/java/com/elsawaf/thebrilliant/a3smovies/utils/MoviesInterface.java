package com.elsawaf.thebrilliant.a3smovies.utils;

import com.elsawaf.thebrilliant.a3smovies.model.MoviesList;
import com.elsawaf.thebrilliant.a3smovies.model.ReviewsList;
import com.elsawaf.thebrilliant.a3smovies.model.TrailersList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesInterface {

    @GET("popular")
    Call<MoviesList> getPopularList(
            @Query(NetworkUtils.PARAM_API_KEY) String apiKey
    );

    @GET("top_rated")
    Call<MoviesList> getTopRatedList(
            @Query(NetworkUtils.PARAM_API_KEY) String apiKey
    );

    @GET("{id}/videos")
    Call<TrailersList> getTrailersList(
            @Path("id") int id,
            @Query(NetworkUtils.PARAM_API_KEY) String apiKey
    );

    @GET("{id}/reviews")
    Call<ReviewsList> getReviewsList(
            @Path("id") int id,
            @Query(NetworkUtils.PARAM_API_KEY) String apiKey
    );
}
