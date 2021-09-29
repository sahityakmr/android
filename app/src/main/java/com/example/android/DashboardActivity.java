package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.myapplication.R;

public class DashboardActivity extends AppCompatActivity {

    Button LogOut;
    TextView EmailShow;
    String EmailHolder;
    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        LogOut = (Button)findViewById(R.id.button);
        EmailShow = (TextView)findViewById(R.id.EmailShow);
        button1 = findViewById(R.id.button1);
       // button1.setOnClickListener(this);


        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra(UserLoginActivity.UserEmail);
        EmailShow.setText(EmailHolder);

       LogOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                if (view.getId() == R.id.button) {
                    finish();
                    Intent intent = new Intent(DashboardActivity.this, UserLoginActivity1.class);

                    startActivity(intent);

                    Toast.makeText(DashboardActivity.this, "Log Out Successfully", Toast.LENGTH_LONG).show();
                }




            }
        });
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent intentt = new Intent(DashboardActivity.this, MainActivity.class);

                startActivity(intentt);
                Toast.makeText(DashboardActivity.this, "WELCOME TO MEENA ELECTRICAL & ENGINEERING WORKS", Toast.LENGTH_LONG).show();
               // intentt.setFlags(intentt.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
