package com.elsawaf.thebrilliant.a3smovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.elsawaf.thebrilliant.a3smovies.model.Movie;
import com.elsawaf.thebrilliant.a3smovies.utils.Constants;
import com.elsawaf.thebrilliant.a3smovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        Movie movie = getIntent().getParcelableExtra(Constants.KEY_MOVIE_DATA);

        String imageUrl = NetworkUtils.buildImageUrl(movie.getPosterPath()).toString();
        Picasso.with(this).load(imageUrl).into(posterImageView);

        ratingTV.setText("" + movie.getUserRating() + "/10");
        usersTV.setText("" + movie.getNumberOfUsers() + " votes");
        dateTV.setText(movie.getReleaseDate());
        titleTV.setText(movie.getOriginalTitle());
        summaryTV.setText(movie.getSummary());
    }
}
