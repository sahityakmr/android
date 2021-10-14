package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

public class Biomatric {
    private String image_name;
    private String fingerprint;
    public Biomatric(String image_name, String fingerprint) {
        this.image_name = image_name;
        this.fingerprint = fingerprint;
    }

    public String getImageId() {
        return image_name;
    }
    public String getfingerPrint() {
        return fingerprint;
    }



}
