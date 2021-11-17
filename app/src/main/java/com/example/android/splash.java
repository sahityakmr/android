package com.example.android;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.mantra.mfs100.MFS100;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//import com.example.myapplication.R;

public class splash extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME_OUT=2000;
    private final String filename2 = "address.txt";
    String file;
    String Temp;
    EditText ss;
    String a ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //This method is used so that your splash activity
        //can cover the entire screen.

        setContentView(R.layout.splash);
        ss = (EditText) findViewById(R.id.splashedit);
        readData();
        //this will bind your MainActivity.class file with activity_main.
        File address = new File(getApplicationContext().getFilesDir(),"address.txt");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if(file.length()==0){
//                    Intent i=new Intent(splash.this,
//                            Credential.class);
//                    //Intent is used to switch from one activity to another.
//
//                    startActivity(i);
//                    //invoke the SecondActivity.
//
//                    finish();
//
//                    //the current activity will get finished.
//
//                }
                  if(address.exists()==false)
                  {
                      Intent i=new Intent(splash.this,
                            MainActivity1.class);


                    startActivity(i);


                    finish();



                }

                else{
                    Intent i=new Intent(splash.this,
                            MainActivity1.class);
                    //Intent is used to switch from one activity to another.

                    startActivity(i);
                    //invoke the SecondActivity.

                    finish();

                    //the current activity will get finished.
                }

                }
        }, SPLASH_SCREEN_TIME_OUT);
    }



    public void printMessage(String m) {
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    private void readData() {
        try {
            FileInputStream fin = openFileInput(filename2);
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char) a);
            }

            // setting text from the file.
            file = temp.toString();
            fin.close();
            // UserLoginFunction(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        printMessage("reading to file " + filename2 + " completed..");
    }


}
