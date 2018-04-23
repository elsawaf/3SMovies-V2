package com.elsawaf.thebrilliant.a3smovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.elsawaf.thebrilliant.a3smovies.BuildConfig;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

    public static MoviesInterface getRetrofitClient(final Context context){
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // Cache network requests for configuration changing and offline access with Retrofit2
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);

        OkHttpClient.Builder httpClient = new OkHttpClient
                .Builder()
                .cache(cache)
                .addInterceptor(new Interceptor() {
                    @Override public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (hasNetworkAccess(context)) {
                            // If there is connectivity, the interceptor will tell the request it can reuse the data for sixty seconds.
                            request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build();
                        } else {
                            // If there's no connectivity, we ask to be given only (only-if-cached) 'stale' data up to 7 days ago
                            request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                        }
                        return chain.proceed(request);
                    }
                });

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
