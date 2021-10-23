package com.example.android.http;

import com.android.volley.VolleyError;

public interface AsyncResponse {
    void postExecute(String response);

    void postError(VolleyError error);
}
