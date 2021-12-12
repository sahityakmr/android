package com.example.android.other;

public interface AsyncResponse {
    default void postExecute(boolean success) {

    }

    default void postExecute(String result) {

    }
}
