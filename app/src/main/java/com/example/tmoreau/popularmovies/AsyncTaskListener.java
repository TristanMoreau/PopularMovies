package com.example.tmoreau.popularmovies;

/**
 * Created by t.moreau on 01/12/2017.
 */

public interface AsyncTaskListener<T> {
    void onTaskStarted();
    void onTaskCompleted(T result);
}
