package com.example.tmoreau.popularmovies;

/**
 * Created by t.moreau on 23/11/2017.
 */

public class Trailer {
    private String mId;
    private String mTitle;
    private String mKey;

    public Trailer(String id, String title, String key) {
        mId = id;
        mTitle = title;
        mKey = key;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
