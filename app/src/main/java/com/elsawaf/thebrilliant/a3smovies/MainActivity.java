package com.elsawaf.thebrilliant.a3smovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.elsawaf.thebrilliant.a3smovies.model.Movie;
import com.elsawaf.thebrilliant.a3smovies.model.MoviesList;
import com.elsawaf.thebrilliant.a3smovies.utils.Constants;
import com.elsawaf.thebrilliant.a3smovies.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, MoviesAdapter.MyOnClickListener {

    private RecyclerView recyclerView;
    private Spinner spinner;
    private ArrayList<Movie> movies;
    private MoviesAdapter adapter;
    private TextView emptyTextView;
    private ProgressBar progressBar;

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
            URL url = NetworkUtils.buildMoviesUrl(NetworkUtils.PATH_POPULAR);
            new GitMoviesListTask().execute(url);
        }
        else {
            Toast.makeText(this, R.string.title_no_network, Toast.LENGTH_SHORT).show();
            toggleEmptyView();
        }
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
        URL url = null;
        switch (pos) {
            case 0:
                url = NetworkUtils.buildMoviesUrl(NetworkUtils.PATH_POPULAR);
                break;
            case 1:
                url = NetworkUtils.buildMoviesUrl(NetworkUtils.PATH_TOP_RATED);
                break;
        }
        new GitMoviesListTask().execute(url);
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

    private class GitMoviesListTask extends AsyncTask<URL, Void, MoviesList>{

        @Override
        protected void onPreExecute() {
//            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            emptyTextView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected MoviesList doInBackground(URL... urls) {
            String response = NetworkUtils.getResponseFromUrl(urls[0]);

            Gson gson = new GsonBuilder().create();
            MoviesList moviesList = gson.fromJson(response, MoviesList.class);
            return moviesList;
        }

        @Override
        protected void onPostExecute(MoviesList moviesList) {
//            super.onPostExecute(moviesList);
            if (moviesList != null){
                movies = (ArrayList<Movie>) moviesList.getResults();
                adapter.updateData(movies);
            }
            progressBar.setVisibility(View.INVISIBLE);
            toggleEmptyView();
        }
    }
}
