package com.elsawaf.thebrilliant.a3smovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.elsawaf.thebrilliant.a3smovies.data.MovieContract.MovieEntry;

public class MovieContentProvider extends ContentProvider {

    // Define final integer constants for the directory of tasks and a single item.
    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    // Declare a static variable for the Uri matcher that we construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Define a static buildUriMatcher method that associates URI's with their int match
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // All paths added to the UriMatcher have a corresponding int.
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }


    // Member variable for a MovieDbHelper that's initialized in the onCreate() method
    private MovieDbHelper mMovieDbHelper;

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor retCursor;
        switch (match) {
            case MOVIES:
                retCursor = db.query(MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                selection = MovieEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{id};
                retCursor = db.query(MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // default case unsupported uri
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase database = mMovieDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);

        Uri returnUri;
        switch (match) {
            case MOVIES:
                // Insert new values into the database
                long id = database.insert(MovieEntry.TABLE_NAME, null, contentValues);

                // Set the value for the returnedUri and write the default case for unknown URI's
                if (id > 0) {
                    // success
                    returnUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, id);
                }
                else {
                    throw new SQLiteException("Failed to insert row into " + uri);
                }
                break;
            // default case unsupported uri
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int moviesDeleted;
        switch (match) {
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                selection = MovieEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{id};
                moviesDeleted = db.delete(MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            // default case unsupported uri
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (moviesDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
