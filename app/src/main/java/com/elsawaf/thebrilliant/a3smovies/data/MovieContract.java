package com.elsawaf.thebrilliant.a3smovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    // The authority, for the Content Provider
    public static final String AUTHORITY = "com.elsawaf.thebrilliant.a3smovies";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // This is the path for the "movies" directory
    public static final String PATH_MOVIES = "movies";

    /* MovieEntry is an inner class that defines the contents of the Movie table */
    public static final class MovieEntry implements BaseColumns {

        // MovieEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        // Movie table and column names
        public static final String TABLE_NAME = "movies";

        // Since MovieEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "posterPath";



    }
}
