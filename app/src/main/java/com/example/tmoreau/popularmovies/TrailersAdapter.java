package com.example.tmoreau.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by t.moreau on 24/11/2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private ArrayList<Trailer> trailersList;
    private TrailersAdapterOnClickHandler mClickHandler;

    public TrailersAdapter(TrailersAdapterOnClickHandler clickHandler) {
        trailersList = new ArrayList<>();
        mClickHandler = clickHandler;
    }

    public interface TrailersAdapterOnClickHandler {
        void onClick(String key);
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleView;
        LinearLayout trailerLayout;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.tv_trailer_title);
            trailerLayout = itemView.findViewById(R.id.ll_trailer_layout);
            trailerLayout.setOnClickListener(this);
        }

        void bind(int position) {
            String title = trailersList.get(position).getmTitle();
            titleView.setText(title);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            String key = trailersList.get(position).getmKey();
            mClickHandler.onClick(key);
        }
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trailers_list_item, parent, false);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
       holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(trailersList == null)
            return 0;
        return trailersList.size();
    }

    public void setTrailersList(ArrayList<Trailer> trailers) {
        trailersList = trailers;
        notifyDataSetChanged();
    }
}
