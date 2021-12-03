package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DashBoard extends AppCompatActivity {

    WebView simpleWebView;
    ProgressBar progressBar;
    String filess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_form);
        Intent intent = getIntent();
        filess = intent.getStringExtra("filess");

        simpleWebView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        String url = filess + "/Android/Payroll_and_Attendance_system/admin";
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.getSettings().setLoadsImagesAutomatically(true);
        simpleWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        simpleWebView.loadUrl(url);
        simpleWebView.setWebViewClient(new MyWebViewClient());

        Toast.makeText(DashBoard.this, "Admin Portal", Toast.LENGTH_SHORT).show();

    }
}
