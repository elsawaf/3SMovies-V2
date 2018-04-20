package com.elsawaf.thebrilliant.a3smovies.utils;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.elsawaf.thebrilliant.a3smovies.data.MovieContract;

// The loader should be static or a top-level class to avoid the memory leak
public class MoviesLoader extends AsyncTaskLoader<Cursor> {
    // Initialize a Cursor, this will hold all the movie data
    Cursor mMovieData = null;

    public MoviesLoader(@NonNull Context context) {
        super(context);
    }

    // loadInBackground() performs asynchronous loading of data
    @Override
    public Cursor loadInBackground() {
        try {
            // this context is the Application Context
            return getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        } catch (Exception e) {
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
            // We have no data, so kick off loading it
            forceLoad();
        }
    }

    // deliverResult sends the result of the load, a Cursor, to the registered listener
    public void deliverResult(Cursor data) {
        // We’ll save the data for later retrieval
        mMovieData = data;
        // We can do any pre-processing we want here
        // Just remember this is on the UI thread so nothing lengthy!
        super.deliverResult(data);
    }
}