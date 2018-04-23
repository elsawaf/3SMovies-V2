package com.elsawaf.thebrilliant.a3smovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.elsawaf.thebrilliant.a3smovies.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by The Brilliant on 02/03/2018.
 */

public class NetworkUtils {

    public static final String TAG = NetworkUtils.class.getSimpleName();
    public static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
    public final static String PATH_POPULAR = "popular";
    public final static String PATH_TOP_RATED = "top_rated";
    final static String PARAM_API_KEY = "api_key";
    public final static String MY_API_KEY = "";

    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    final static String PATH_IMAGE_SIZE = "w342";

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

    public static MoviesInterface getRetrofitClient(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(loggingInterceptor);
        }

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(MOVIES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();

        MoviesInterface client = retrofit.create(MoviesInterface.class);

        return client;
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
