package com.example.tmoreau.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tmoreau.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class MovieActivity extends AppCompatActivity {

    private static final String TAG = MovieActivity.class.getSimpleName();

    private Movie movie;

    private ImageView posterView;
    private TextView titleView;
    private TextView overviewView;
    private TextView userRatingView;
    private TextView releaseDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            Bundle data = getIntent().getExtras();
            movie = data.getParcelable("movie");
        } catch (Exception e) {
            e.printStackTrace();
        }

        posterView = findViewById(R.id.iv_movie_poster);
        titleView = findViewById(R.id.tv_movie_title);
        overviewView = findViewById(R.id.tv_movie_overview);
        userRatingView = findViewById(R.id.tv_movie_rating);
        releaseDateView = findViewById(R.id.tv_movie_release_date);

        if (movie != null)
            displayMovieDetails();
    }

    private void displayMovieDetails() {
        titleView.setText(movie.getTitle());
        URL imageURL = NetworkUtils.buildImageUrl(movie.getImage());
        Picasso.with(getApplicationContext()).load(imageURL.toString()).fit().centerCrop().into(posterView);
        overviewView.setText(movie.getOverview());
        String releaseDateStr = movie.getReleaseDate().substring(0, 4);
        releaseDateView.setText(releaseDateStr);
        String ratingStr = movie.getRating() + " / 10";
        userRatingView.setText(ratingStr);
    }
}
