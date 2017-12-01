package com.example.tmoreau.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by t.moreau on 21/11/2017.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIES_BASE_URL =
            "http://api.themoviedb.org/3/movie";

    /*private static final String POPULAR_MOVIES_URL = MOVIES_BASE_URL +
            "/movie/popular";

    private static final String TOP_RATED_MOVIES_URL = MOVIES_BASE_URL +
            "/movie/top_rated";*/

    private static final String VIDEO_PATH = "videos";
    private static final String IMAGE_BASE_URL =
            "http://image.tmdb.org/t/p/w185";

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";
    private static final String YOUTUBE_WATCH_PARAM = "v";

    // replace this with your personal API key
    private static final String API_KEY = "";

    private static final String API_KEY_PARAM = "api_key";

    public static URL buildMoviesListUrl(String sortBy) {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildYouTubeUrl(String key) {
        Uri builtUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_WATCH_PARAM, key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildVideosUrl(String sortBy) {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendPath(VIDEO_PATH)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildImageUrl(String imageQuery) {
        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendEncodedPath(imageQuery)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built image URI " + url);

        return url;
    }

    public static URL buildMovieUrl(String movieId) {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
