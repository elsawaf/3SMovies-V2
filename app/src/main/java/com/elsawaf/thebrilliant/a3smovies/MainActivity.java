package com.elsawaf.thebrilliant.a3smovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elsawaf.thebrilliant.a3smovies.model.Movie;
import com.elsawaf.thebrilliant.a3smovies.model.MoviesList;
import com.elsawaf.thebrilliant.a3smovies.utils.Constants;
import com.elsawaf.thebrilliant.a3smovies.utils.MoviesLoader;
import com.elsawaf.thebrilliant.a3smovies.utils.NetworkUtils;
import com.elsawaf.thebrilliant.a3smovies.utils.ScreenUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        MoviesAdapter.MyOnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "elsawafApp";
    private RecyclerView recyclerView;
    private ArrayList<Movie> movies;
    private MoviesAdapter adapter;
    private TextView emptyTextView;
    private ProgressBar progressBar;

    private static final int MOVIE_LOADER_ID = 0;
    // this to track the movies list that the user choice
    private int mUserChoice;

    public static final String KEY_STATE_RV_POSITION = "recyclerViewPosition";
    public static final String KEY_STATE_USER_CHOICE = "userListChoice";
    Parcelable mSavedRecyclerLayoutState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        emptyTextView = findViewById(R.id.text_view_empty);
        progressBar = findViewById(R.id.progress_bar_movies);

        movies =  new ArrayList<>();
        adapter = new MoviesAdapter(movies, this, this);
        recyclerView.setAdapter(adapter);

        // Rather than hard-code the values in specific numbers i.e 2, we will calculate
        // the no. of possible columns at runtime and then use that to set on our GridLayoutManager.
        int numberOfColumns = ScreenUtils.calculateNoOfColumns(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        recyclerView.setLayoutManager(layoutManager);

        if (savedInstanceState != null) {
            // restore list state if activity is recreated then will using it after load the data
            mSavedRecyclerLayoutState = savedInstanceState.getParcelable(KEY_STATE_RV_POSITION);
            mUserChoice = savedInstanceState.getInt(KEY_STATE_USER_CHOICE, 0);
            displayMoviesList(mUserChoice);
        }
        else if (NetworkUtils.hasNetworkAccess(this)){
            mUserChoice = 0;
            displayMoviesList(mUserChoice);
//            makeRetrofitCall(Constants.SORT_MOVIES_BY_POPULAR);
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
                    MoviesList list = response.body();
                    movies = (ArrayList<Movie>) list.getResults();
                    adapter.updateData(movies);
                    // we need to add adapter in case of user was choice the favorites list
                    recyclerView.setAdapter(adapter);
                    if (mSavedRecyclerLayoutState != null) {
                        retrieveListState();
                    }
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

    private void displayMoviesList(int choice) {
        switch (choice) {
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the list state
        outState.putParcelable(KEY_STATE_RV_POSITION, recyclerView.getLayoutManager().onSaveInstanceState());
        // save the use choice
        outState.putInt(KEY_STATE_USER_CHOICE, mUserChoice);
    }

    private void retrieveListState() {
        // apply list state after we added the data
        recyclerView.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
        // to prevent app applying this state if user choice another list
        mSavedRecyclerLayoutState = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_popular_list:
                displayMoviesList(0);
                break;
            case R.id.action_top_rated:
                displayMoviesList(1);
                break;
            default:
                displayMoviesList(2);
                break;
        }
        return true;
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Constants.KEY_MOVIE_DATA, movies.get(position));
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.i(TAG, "onCreateLoader: ");
        return new MoviesLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // to prevent displaying favourites list each time user come back to activity
        // even if he was choice another list
        if (mUserChoice == 2) {
            MoviesCursorAdapter favouriteMoviesAdapter = new MoviesCursorAdapter(data, this);
            recyclerView.setAdapter(favouriteMoviesAdapter);
            if (mSavedRecyclerLayoutState != null) {
                retrieveListState();
            }
        }
        Log.i(TAG, "onLoadFinished: ");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.i(TAG, "onLoaderReset: ");
    }
}
