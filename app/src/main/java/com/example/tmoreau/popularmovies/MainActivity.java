package com.example.tmoreau.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tmoreau.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesListAdapter.MoviesListAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressBar mPbLoadingIndicator;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private MoviesListAdapter mMovieListAdapter;

    private ArrayList<Movie> mMoviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPbLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mRecyclerView = findViewById(R.id.rv_movies_list);

        mGridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mMovieListAdapter = new MoviesListAdapter(this, this);
        mRecyclerView.setAdapter(mMovieListAdapter);

        mMoviesList = new ArrayList<>();

        loadMovieData("popular");
    }

    private void loadMovieData(String sortBy) {
        new FetchMoviesTask().execute(sortBy);
    }

    @Override
    public void onClick(String movieId) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, movieId);
        startActivity(intent);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPbLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... strings) {
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

                mMoviesList.clear();

                for (int i = 0; i < movies.length(); i++) {
                    JSONObject movie = movies.getJSONObject(i);

                    String id = movie.getString("id");
                    String image = movie.getString("poster_path");

                    Movie movieObject = new Movie(id, image);
                    mMoviesList.add(movieObject);
                }

                return null;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mPbLoadingIndicator.setVisibility(View.INVISIBLE);
            mMovieListAdapter.setMoviesList(mMoviesList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.sort_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    // COMPLETED (7) Override onOptionsItemSelected to handle clicks on the refresh button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_by_popular) {
            loadMovieData("popular");
            return true;
        }

        if (id == R.id.action_sort_by_rating) {
            loadMovieData("top_rated");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}