package com.example.tmoreau.popularmovies;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements MoviesListAdapter.MoviesListAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressBar mPbLoadingIndicator;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private MoviesListAdapter mMovieListAdapter;

    //private static final int MOVIES_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(getApplication());

        mPbLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mRecyclerView = findViewById(R.id.rv_movies_list);

        mGridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mMovieListAdapter = new MoviesListAdapter(this, this);
        mRecyclerView.setAdapter(mMovieListAdapter);

        getMovies("popular");
    }

    @Override
    public void onClick(String movieId) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, movieId);
        startActivity(intent);
    }

    public void getMovies(String sortBy) {
        showSpinner();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        URL url = NetworkUtils.buildMoviesListUrl(sortBy);

        Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    Log.v(TAG, "Response : " + response);
                    final ArrayList<Movie> moviesList = new ArrayList<>();

                    try {

                        Thread.sleep(4000);
                        JSONObject jsonMoviesList = new JSONObject(response.body().string());
                        JSONArray movies = jsonMoviesList.getJSONArray("results");

                        for (int i = 0; i < movies.length(); i++) {
                            JSONObject movie = movies.getJSONObject(i);

                            String id = movie.getString("id");
                            String image = movie.getString("poster_path");

                            Movie movieObject = new Movie(id, image);
                            moviesList.add(movieObject);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mMovieListAdapter.setMoviesList(moviesList);
                                    hideSpinner();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showSpinner() {
        mPbLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void hideSpinner() {
        mPbLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
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
            getMovies("popular");

            return true;
        }

        if (id == R.id.action_sort_by_rating) {
            getMovies("top_rated");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}