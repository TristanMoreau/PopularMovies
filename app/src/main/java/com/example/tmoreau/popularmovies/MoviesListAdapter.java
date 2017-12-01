package com.example.tmoreau.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tmoreau.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by t.moreau on 22/11/2017.
 */

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MovieViewHolder> {

    private Context mContext;
    private ArrayList<Movie> mMoviesList;

    private final MoviesListAdapterOnClickHandler mClickHandler;

    public interface MoviesListAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MoviesListAdapter(Context context, MoviesListAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        mMoviesList = new ArrayList<>();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            String imagePath = mMoviesList.get(position).getImage();
            URL imageURL = NetworkUtils.buildImageUrl(imagePath);
            Picasso.with(mContext).load(imageURL.toString()).fit().centerCrop().into(imageView);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Movie movie = mMoviesList.get(position);
            mClickHandler.onClick(movie);
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movies_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int position) {
        movieViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mMoviesList == null)
            return 0;
        return mMoviesList.size();
    }

    public void setMoviesList(ArrayList<Movie> moviesList) {
        mMoviesList = moviesList;
        notifyDataSetChanged();
    }
}