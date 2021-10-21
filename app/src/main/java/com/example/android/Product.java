package com.example.android;

import java.io.Serializable;

public class Product implements Serializable {
    private String image_path;
    private String mobile;
    private String aadhar_no;
   // private double rating;
    private String l_name;
    private String image_name;
    private String f_name;
    private String fileContent;
    public Product(String f_name, String mobile, String aadhar_no, String image_name, String l_name, String image_path) {
        this.f_name = f_name;
        this.mobile = mobile;
        this.aadhar_no = aadhar_no;
        this.image_name = image_name;
        this.l_name = l_name;
        this.image_path= image_path;
        this.fileContent= fileContent;
    }
 
    public String getId() {
        return f_name;
    }
 
    public String getTitle() {
        return mobile;
    }
 
    public String getShortdesc() {
        return aadhar_no;
    }
 
    public String getRating() {
        return image_name;
    }
 
    public String getPrice() {return l_name;}



    public String getImage() {return image_path;}

   // public String getFileContent() {return fileContent;}
}