package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.myapplication.R;

public class mark_attendance_dashboard extends AppCompatActivity {

    Button LogOut;
  //  TextView EmailShow;
    TextView fingerShow;
    String EmailHolder;
    String FINGERSTRINGHolder;
    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mark_attendance_dashboard);


        //LogOut = (Button)findViewById(R.id.button);
        fingerShow = (TextView)findViewById(R.id.EmailShow);
//        EmailShow = (TextView)findViewById(R.id.EmailShow);
        button1 = findViewById(R.id.button1);
        // button1.setOnClickListener(this);


        Intent intent = getIntent();
//        EmailHolder = intent.getStringExtra(UserLoginActivity.UserEmail);
//        EmailShow.setText(EmailHolder);
          FINGERSTRINGHolder = intent.getStringExtra(mark_attendance.userFinger);
          fingerShow.setText(FINGERSTRINGHolder);
          Handler handler = new Handler();
          handler.postDelayed(new Runnable(){
              public void run() {
                  Intent intentt = new Intent(mark_attendance_dashboard.this, MainActivity1.class);
//                intentt.setFlags(intentt.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentt);
                Toast.makeText(mark_attendance_dashboard.this, "WELCOME TO MEENA ELECTRICAL & ENGINEERING WORKS", Toast.LENGTH_LONG).show();
//
              }
          },10000);

//        button1.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                finish();
//                Intent intentt = new Intent(mark_attendance_dashboard.this, MainActivity1.class);
//                intentt.setFlags(intentt.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intentt);
//                Toast.makeText(mark_attendance_dashboard.this, "WELCOME TO MEENA ELECTRICAL & ENGINEERING WORKS", Toast.LENGTH_LONG).show();
//
//                finish();
//            }
//        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
