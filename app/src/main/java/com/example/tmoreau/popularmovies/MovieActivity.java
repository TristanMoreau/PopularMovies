package com.example.tmoreau.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tmoreau.popularmovies.utilities.NetworkUtils;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieActivity extends AppCompatActivity implements TrailersAdapter.TrailersAdapterOnClickHandler {

    private static final String TAG = MovieActivity.class.getSimpleName();

    public final static String LIST_STATE_KEY = "recycler_list_state";

    private OkHttpClient okHttpClient;

    private Movie movie;

    private Bundle movieDetailsState;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TrailersAdapter trailersAdapter;
    Parcelable listState;

    private ImageView posterView;
    private TextView titleView;
    private TextView overviewView;
    private TextView userRatingView;
    private TextView releaseDateView;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        String movieId = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        recyclerView = findViewById(R.id.rv_trailers_list);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        trailersAdapter = new TrailersAdapter(this);
        recyclerView.setAdapter(trailersAdapter);

        posterView = findViewById(R.id.iv_movie_poster);
        titleView = findViewById(R.id.tv_movie_title);
        overviewView = findViewById(R.id.tv_movie_overview);
        userRatingView = findViewById(R.id.tv_movie_rating);
        releaseDateView = findViewById(R.id.tv_movie_release_date);

        if (savedInstanceState == null)
            loadMovieDetails(movieId);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        listState = linearLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, listState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null)
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (listState != null) {
            linearLayoutManager.onRestoreInstanceState(listState);
        }
    }

    private void loadMovieDetails(String movieId) {
        movie = new Movie();

        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        getMovie(movieId);
        getTrailers(movieId);
    }

    @Override
    public void onClick(String key) {
        URL url = NetworkUtils.buildYouTubeUrl(key);
        Uri webpage = Uri.parse(url.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void getMovie(final String movieId) {
        URL url = NetworkUtils.buildMovieUrl(movieId);
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

                    try {

                        Thread.sleep(4000);
                        JSONObject jsonMovie = new JSONObject(response.body().string());

                        Log.v(TAG, "Response : " + jsonMovie);

                        String id = jsonMovie.getString("id");
                        String title = jsonMovie.getString("original_title");
                        String overview = jsonMovie.getString("overview");
                        String imagePath = jsonMovie.getString("poster_path");
                        String releaseDate = jsonMovie.getString("release_date");
                        String rating = jsonMovie.getString("vote_average");

                        movie.setId(id);
                        movie.setTitle(title);
                        movie.setOverview(overview);
                        movie.setImage(imagePath);
                        movie.setReleaseDate(releaseDate);
                        movie.setRating(rating);
                        movie.setHasVideo(true);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            displayMovieDetails();
                            }
                        });
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    private void displayMovieDetails() {
        if (movie == null)
            return;

        titleView.setText(movie.getTitle());
        URL imageURL = NetworkUtils.buildImageUrl(movie.getImage());
        Picasso.with(getApplicationContext()).load(imageURL.toString()).fit().centerCrop().into(posterView);
        overviewView.setText(movie.getOverview());
        String releaseDateStr = movie.getReleaseDate().substring(0, 4);
        releaseDateView.setText(releaseDateStr);
        String ratingStr = movie.getRating() + " / 10";
        userRatingView.setText(ratingStr);
    }

    private void getTrailers(String movieId) {
        URL url = NetworkUtils.buildVideosUrl(movieId);
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
                      if (!response.isSuccessful()) {
                          throw new IOException("Unexpected code " + response);
                      } else {
                          Log.v(TAG, "Response : " + response);

                          try {
                              Thread.sleep(4000);
                              JSONObject jsonTrailers = new JSONObject(response.body().string());
                              JSONArray jsonTrailersList = jsonTrailers.getJSONArray("results");

                              for (int i = 0; i < jsonTrailersList.length(); i++) {
                                  JSONObject jsonTrailer = jsonTrailersList.getJSONObject(i);

                                  String trailerId = jsonTrailer.getString("id");
                                  String trailerTitle = jsonTrailer.getString("name");
                                  String trailerKey = jsonTrailer.getString("key");
                                  movie.addTrailer(new Trailer(trailerId, trailerTitle, trailerKey));
                              }

                              if (jsonTrailersList.length() == 0)
                                  movie.setHasVideo(false);

                              runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      trailersAdapter.setTrailersList(movie.getTrailers());
                                  }
                              });

                          } catch (Exception e) {
                              e.printStackTrace();
                          }
                      }
                  }
              }
        });
    }

    private void displayTrailers() {

    }
}
