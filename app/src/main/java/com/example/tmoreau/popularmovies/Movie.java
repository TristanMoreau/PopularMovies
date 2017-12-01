package com.example.tmoreau.popularmovies;

import java.util.ArrayList;

/**
 * Created by t.moreau on 23/11/2017.
 */

public class Movie {
    private String mId;
    private String mTitle;
    private String mImage;
    private String mOverview;
    private String mReleaseDate;
    private String mRating;
    private boolean mHasVideo;
    private ArrayList<Trailer> mTrailers;

    public Movie() {
        mTrailers = new ArrayList<>();
    }

    public Movie(String id, String image) {
        mId = id;
        mImage = image;
        mTrailers = new ArrayList<>();
    }

    public Movie(String mId, String mTitle, String mImage, String mOverview, String mReleaseDate, String mRating, boolean hasVideo) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mImage = mImage;
        this.mOverview = mOverview;
        this.mReleaseDate = mReleaseDate;
        this.mRating = mRating;
        this.mHasVideo = hasVideo;
        mTrailers = new ArrayList<>();
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public void setRating(String rating) {
        mRating = rating;
    }

    public String getRating() { return mRating; }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public void setHasVideo(boolean hasVideo) {
        mHasVideo = hasVideo;
    }

    public boolean hasVideo() {
        return mHasVideo;
    }

    public ArrayList<Trailer> getTrailers() {
        return mTrailers;
    }

    public void addTrailer(Trailer trailer) {
        mTrailers.add(trailer);
    }

    public int getNumberOfTrailers() {
        return mTrailers.size();
    }
}
