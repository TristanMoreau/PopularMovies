package com.example.tmoreau.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    private TextView mConnectionErrorTextView;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private MoviesListAdapter mMovieListAdapter;

    private boolean mIsConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPbLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mConnectionErrorTextView = findViewById(R.id.tv_connection_error);
        mRecyclerView = findViewById(R.id.rv_movies_list);

        mGridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mMovieListAdapter = new MoviesListAdapter(this, this);
        mRecyclerView.setAdapter(mMovieListAdapter);
        mConnectionErrorTextView.setText(R.string.connection_error);

        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            mIsConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        } catch(Exception e) {
            e.printStackTrace();
        }
        loadMovieData("popular");
    }

    private void loadMovieData(String sortBy) {
        if (mIsConnected) {
            mConnectionErrorTextView.setVisibility(View.GONE);
            new FetchMoviesTask(new FetchMoviesTaskListener()).execute(sortBy);
        } else {
            mConnectionErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(String movieId) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, movieId);
        startActivity(intent);
    }

    public class FetchMoviesTaskListener implements AsyncTaskListener<ArrayList<Movie>> {
        @Override
        public void onTaskStarted() {
            mPbLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTaskCompleted(ArrayList<Movie> result) {
            mPbLoadingIndicator.setVisibility(View.GONE);
            if (result != null)
                mMovieListAdapter.setMoviesList(result);
            else
                mConnectionErrorTextView.setVisibility(View.VISIBLE);
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