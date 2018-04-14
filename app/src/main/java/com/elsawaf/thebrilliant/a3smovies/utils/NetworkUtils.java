package com.elsawaf.thebrilliant.a3smovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by The Brilliant on 02/03/2018.
 */

public class NetworkUtils {

    public static final String TAG = NetworkUtils.class.getSimpleName();
    public static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";
    public final static String PATH_POPULAR = "popular";
    public final static String PATH_TOP_RATED = "top_rated";
    final static String PARAM_API_KEY = "api_key";
    final static String MY_API_KEY = "";

    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    final static String PATH_IMAGE_SIZE = "w342";

    public static URL buildMoviesUrl (String sortBy){
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(PARAM_API_KEY, MY_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "buildUrl: " + url);
        return url;
    }

    public static URL buildImageUrl (String posterPath){
        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(PATH_IMAGE_SIZE)
                .appendEncodedPath(posterPath)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "buildUrl: " + url);
        return url;
    }

    public static String getResponseFromUrl (URL url) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput)
                return scanner.next();
            else
                return null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return null;
    }

    public static boolean hasNetworkAccess(Context context){
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
