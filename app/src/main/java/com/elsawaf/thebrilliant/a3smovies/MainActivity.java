package com.elsawaf.thebrilliant.a3smovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.elsawaf.thebrilliant.a3smovies.data.MovieContract;
import com.elsawaf.thebrilliant.a3smovies.model.Movie;
import com.elsawaf.thebrilliant.a3smovies.model.MoviesList;
import com.elsawaf.thebrilliant.a3smovies.utils.Constants;
import com.elsawaf.thebrilliant.a3smovies.utils.NetworkUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, MoviesAdapter.MyOnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "elsawafApp";
    private RecyclerView recyclerView;
    private Spinner spinner;
    private ArrayList<Movie> movies;
    private MoviesAdapter adapter;
    private TextView emptyTextView;
    private ProgressBar progressBar;

    private static final int MOVIE_LOADER_ID = 0;
    // this to track the movies list that the user choice
    private int mUserChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        emptyTextView = findViewById(R.id.text_view_empty);
        spinner = findViewById(R.id.spinner_movies);
        progressBar = findViewById(R.id.progress_bar_movies);

        movies =  new ArrayList<>();
        adapter = new MoviesAdapter(movies, this, this);
        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        spinner.setOnItemSelectedListener(this);

        if (NetworkUtils.hasNetworkAccess(this)){
            mUserChoice = 0;
            makeRetrofitCall(Constants.SORT_MOVIES_BY_POPULAR);
        }
        else {
            Toast.makeText(this, R.string.title_no_network, Toast.LENGTH_SHORT).show();
            toggleEmptyView();
        }
    }

    private void makeRetrofitCall(int sortBy) {
        showProgressPar();

        Call<MoviesList> call;
        if (sortBy == Constants.SORT_MOVIES_BY_POPULAR)
            call = NetworkUtils.getRetrofitClient().getPopularList(NetworkUtils.MY_API_KEY);
        else
            call = NetworkUtils.getRetrofitClient().getTopRatedList(NetworkUtils.MY_API_KEY);

        call.enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                if (response != null){
                    Log.d(TAG, "onResponse: " + response.code());
                    MoviesList list = response.body();
                    movies = (ArrayList<Movie>) list.getResults();
                    adapter.updateData(movies);
                    recyclerView.setAdapter(adapter);
                }
                progressBar.setVisibility(View.INVISIBLE);
                toggleEmptyView();
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                movies = null;
                progressBar.setVisibility(View.INVISIBLE);
                toggleEmptyView();
            }
        });
    }

    private void showProgressPar() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        emptyTextView.setVisibility(View.INVISIBLE);
    }

    private void toggleEmptyView() {
        if (movies == null || movies.isEmpty()){
            emptyTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
        else {
            emptyTextView.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        switch (pos) {
            case 0:
                mUserChoice = 0;
                makeRetrofitCall(Constants.SORT_MOVIES_BY_POPULAR);
                break;
            case 1:
                mUserChoice = 1;
                makeRetrofitCall(Constants.SORT_MOVIES_BY_TOPRATED);
                break;
            case 2:
                mUserChoice = 2;
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Constants.KEY_MOVIE_DATA, movies.get(position));
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<Cursor>(this) {
            // Initialize a Cursor, this will hold all the movie data
            Cursor mMovieData = null;

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                }
                catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mMovieData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // to prevent loading favourites list each time user come back to activity
        if (mUserChoice == 2) {
            MoviesCursorAdapter favouriteMoviesAdapter = new MoviesCursorAdapter(data, this);
            recyclerView.setAdapter(favouriteMoviesAdapter);
        }
        Log.i(TAG, "onLoadFinished: ");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.i(TAG, "onLoaderReset: ");
    }
}
