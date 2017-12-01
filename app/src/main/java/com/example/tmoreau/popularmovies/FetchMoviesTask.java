package com.example.tmoreau.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.tmoreau.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by t.moreau on 01/12/2017.
 */

public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private static final String TAG = FetchMoviesTask.class.getSimpleName();

    private AsyncTaskListener<ArrayList<Movie>> mListener;

    public FetchMoviesTask(AsyncTaskListener<ArrayList<Movie>> listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.onTaskStarted();
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... strings) {
        if (strings.length == 0)
            return null;

        String sortBy = strings[0];
        URL movieRequestUrl = NetworkUtils.buildMoviesListUrl(sortBy);

        try {
            String jsonMovieResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieRequestUrl);

            Log.v(TAG, "Response : " + jsonMovieResponse);

            JSONObject jsonMoviesList = new JSONObject(jsonMovieResponse);

            JSONArray movies = jsonMoviesList.getJSONArray("results");

            ArrayList<Movie> moviesList = new ArrayList<>();

            for (int i = 0; i < movies.length(); i++) {
                JSONObject jsonMovie = movies.getJSONObject(i);

                String id = jsonMovie.getString("id");
                String title = jsonMovie.getString("original_title");
                String overview = jsonMovie.getString("overview");
                String imagePath = jsonMovie.getString("poster_path");
                String releaseDate = jsonMovie.getString("release_date");
                String rating = jsonMovie.getString("vote_average");
                boolean hasVideo = jsonMovie.getBoolean("video");

                Movie movieObject = new Movie(id, title, imagePath, overview, releaseDate, rating, hasVideo);
                moviesList.add(movieObject);
            }

            return moviesList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> result) {
        super.onPostExecute(result);

        mListener.onTaskCompleted(result);
    }
}