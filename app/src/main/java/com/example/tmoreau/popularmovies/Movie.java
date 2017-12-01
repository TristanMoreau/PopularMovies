package com.example.tmoreau.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import static java.lang.System.out;

/**
 * Created by t.moreau on 23/11/2017.
 */

public class Movie implements Parcelable {
    private String mId;
    private String mTitle;
    private String mImage;
    private String mOverview;
    private String mReleaseDate;
    private String mRating;
    private boolean mHasVideo;


    public Movie(String mId, String mTitle, String mImage, String mOverview, String mReleaseDate, String mRating, boolean hasVideo) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mImage = mImage;
        this.mOverview = mOverview;
        this.mReleaseDate = mReleaseDate;
        this.mRating = mRating;
        this.mHasVideo = hasVideo;
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

    public String getRating() { return mRating; }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public boolean hasVideo() {
        return mHasVideo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mTitle);
        dest.writeString(this.mImage);
        dest.writeString(this.mOverview);
        dest.writeString(this.mReleaseDate);
        dest.writeString(this.mRating);
        dest.writeByte(this.mHasVideo ? (byte) 1 : (byte) 0);
    }

    protected Movie(Parcel in) {
        this.mId = in.readString();
        this.mTitle = in.readString();
        this.mImage = in.readString();
        this.mOverview = in.readString();
        this.mReleaseDate = in.readString();
        this.mRating = in.readString();
        this.mHasVideo = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
