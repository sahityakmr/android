package com.example.android;

public class Biomatric {
    String imageId;
    String[] fingerPrint;
    public Biomatric(String imageid, String[] fingerPrint) {
        this.imageId = imageid;
        this.fingerPrint = fingerPrint;
    }

    public String getImageId() {
        return imageId;
    }
    public String[] getfingerPrint() {
        return fingerPrint;
    }



}
