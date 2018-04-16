package com.elsawaf.thebrilliant.a3smovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elsawaf.thebrilliant.a3smovies.model.Movie;
import com.elsawaf.thebrilliant.a3smovies.model.MovieTrailer;
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

public class DetailsActivity extends AppCompatActivity implements TrailersAdapter.MyOnClickListener {

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
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private ArrayList<MovieTrailer> movieTrailers;
    private TrailersAdapter adapter;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        movie = getIntent().getParcelableExtra(Constants.KEY_MOVIE_DATA);

        String imageUrl = NetworkUtils.buildImageUrl(movie.getPosterPath()).toString();
        Picasso.with(this).load(imageUrl).into(posterImageView);

        ratingTV.setText("" + movie.getUserRating() + "/10");
        usersTV.setText("" + movie.getNumberOfUsers() + " votes");
        dateTV.setText(movie.getReleaseDate());
        titleTV.setText(movie.getOriginalTitle());
        summaryTV.setText(movie.getSummary());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        makeTrailersCall();
    }

    private void makeTrailersCall() {
        Call<TrailersList> call = NetworkUtils.getRetrofitClient().getTrailersList(String.valueOf(movie.getId()), NetworkUtils.MY_API_KEY);
        call.enqueue(new Callback<TrailersList>() {
            @Override
            public void onResponse(Call<TrailersList> call, Response<TrailersList> response) {
                if (response != null) {
                    TrailersList list = response.body();
                    movieTrailers = (ArrayList<MovieTrailer>) list.getResults();
                    adapter = new TrailersAdapter(movieTrailers, DetailsActivity.this, DetailsActivity.this);
                    recyclerView.setAdapter(adapter);
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
}
