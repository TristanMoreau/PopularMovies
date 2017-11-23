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
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        String movieId = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        posterView = findViewById(R.id.iv_movie_poster);
        titleView = findViewById(R.id.tv_movie_title);
        overviewView = findViewById(R.id.tv_movie_overview);
        userRatingView = findViewById(R.id.tv_movie_rating);
        releaseDateView = findViewById(R.id.tv_movie_release_date);

        new FetchMovieTask().execute(movieId);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            if (strings[0] == null)
                return null;

            String movieId = strings[0];
            URL movieRequestUrl = NetworkUtils.buildMovieUrl(movieId);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);

                Log.v(TAG, "Response : " + jsonMovieResponse);

                JSONObject jsonMovie = new JSONObject(jsonMovieResponse);

                String id = jsonMovie.getString("id");
                String title = jsonMovie.getString("original_title");
                String overview = jsonMovie.getString("overview");
                String imagePath = jsonMovie.getString("poster_path");
                String releaseDate = jsonMovie.getString("release_date");
                String rating = jsonMovie.getString("vote_average");
                boolean hasVideo = jsonMovie.getBoolean("video");

                movie = new Movie(id, title, imagePath, overview, releaseDate, rating, hasVideo);

                return null;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

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
}
