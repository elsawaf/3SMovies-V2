package com.elsawaf.thebrilliant.a3smovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elsawaf.thebrilliant.a3smovies.data.MovieContract;
import com.elsawaf.thebrilliant.a3smovies.model.Movie;
import com.elsawaf.thebrilliant.a3smovies.model.MovieReview;
import com.elsawaf.thebrilliant.a3smovies.model.MovieTrailer;
import com.elsawaf.thebrilliant.a3smovies.model.ReviewsList;
import com.elsawaf.thebrilliant.a3smovies.model.TrailersList;
import com.elsawaf.thebrilliant.a3smovies.utils.Constants;
import com.elsawaf.thebrilliant.a3smovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements TrailersAdapter.MyOnClickListener,
        View.OnClickListener {

    @BindView(R.id.image_view_poster)
    ImageView posterImageView;
    @BindView(R.id.text_view_rating)
    TextView ratingTV;
    @BindView(R.id.text_view_users)
    TextView usersTV;
    @BindView(R.id.text_view_date)
    TextView dateTV;
    @BindView(R.id.text_view_title)
    TextView titleTV;
    @BindView(R.id.text_view_summary)
    TextView summaryTV;
    @BindView(R.id.trailers_recycler_view)
    RecyclerView trailersRecyclerView;
    @BindView(R.id.reviews_recycler_view)
    RecyclerView reviewsRecyclerView;
    @BindView(R.id.addToFavouriteBtn)
    Button addToFavouriteBtn;

    private ArrayList<MovieTrailer> movieTrailers;
    private TrailersAdapter trailersAdapter;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        addToFavouriteBtn.setOnClickListener(this);

        movie = getIntent().getParcelableExtra(Constants.KEY_MOVIE_DATA);

        String imageUrl = NetworkUtils.buildImageUrl(movie.getPosterPath()).toString();
        Picasso.with(this).load(imageUrl).into(posterImageView);

        ratingTV.setText("" + movie.getUserRating() + "/10");
        usersTV.setText("" + movie.getNumberOfUsers() + " votes");
        dateTV.setText(movie.getReleaseDate());
        titleTV.setText(movie.getOriginalTitle());
        summaryTV.setText(movie.getSummary());

        checkFavouritesMovies(movie.getId());

        LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(this);
        trailersRecyclerView.setLayoutManager(trailersLayoutManager);
        makeTrailersCall();

        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this);
        reviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
        makeReviewsCall();

    }

    private void makeReviewsCall() {
        Call<ReviewsList> call = NetworkUtils.getRetrofitClient().getReviewsList(movie.getId(), NetworkUtils.MY_API_KEY);
        call.enqueue(new Callback<ReviewsList>() {
            @Override
            public void onResponse(Call<ReviewsList> call, Response<ReviewsList> response) {
                ReviewsList list = response.body();
                ArrayList<MovieReview> movieReviews = (ArrayList<MovieReview>) list.getResults();
                ReviewsAdapter reviewsAdapter = new ReviewsAdapter(movieReviews);
                reviewsRecyclerView.setAdapter(reviewsAdapter);
            }

            @Override
            public void onFailure(Call<ReviewsList> call, Throwable t) {

            }
        });
    }

    private void makeTrailersCall() {
        Call<TrailersList> call = NetworkUtils.getRetrofitClient().getTrailersList(movie.getId(), NetworkUtils.MY_API_KEY);
        call.enqueue(new Callback<TrailersList>() {
            @Override
            public void onResponse(Call<TrailersList> call, Response<TrailersList> response) {
                if (response != null) {
                    TrailersList list = response.body();
                    movieTrailers = (ArrayList<MovieTrailer>) list.getResults();
                    trailersAdapter = new TrailersAdapter(movieTrailers, DetailsActivity.this, DetailsActivity.this);
                    trailersRecyclerView.setAdapter(trailersAdapter);
                }
            }

            @Override
            public void onFailure(Call<TrailersList> call, Throwable t) {
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        MovieTrailer movieTrailer = movieTrailers.get(position);
        String key = movieTrailer.getKey();
        openTheVideo(key);
    }

    public void openTheVideo(String videoKey) {
        String url = "https://www.youtube.com/watch?v=" + videoKey;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

        if (movie.isFavourite()) {
            // delete from database
            String stringId = Integer.toString(movie.getId());
            Uri uri = MovieContract.MovieEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();

            int moviesDeleted = getContentResolver().delete(uri, null, null);
            if (moviesDeleted > 0) {
                movie.setFavourite(false);
                toggleFavouriteBtn();
            }
            return;
        }

        // else then add to database
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getOriginalTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());

        // Insert new movie data via a ContentResolver
        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        if (uri != null) {
            // added success
            movie.setFavourite(true);
            toggleFavouriteBtn();
        }
    }

    private void toggleFavouriteBtn() {
        if (movie.isFavourite()) {
            addToFavouriteBtn.setText("Added");
        }
        else {
            addToFavouriteBtn.setText("Add");
        }
    }

    private boolean checkFavouritesMovies(int id) {
        String stringId = Integer.toString(id);
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        Cursor cursor = getContentResolver().query(uri,
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.getCount() > 0) {
            movie.setFavourite(true);
            toggleFavouriteBtn();
            return true;
        }
        return false;
    }
}
