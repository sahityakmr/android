package com.example.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileOutputStream;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;
import com.nightonke.boommenu.BoomMenuButton;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Credential extends AppCompatActivity {

    Button connect;
    EditText credential;
    String Temp;
    Boolean CheckEditText ;
    private final String filename = "address.txt";
    ProgressDialog progressDialog;
    HttpParse httpParse = new HttpParse();
    private long pressedTime;
    String finalResult ;
    TextView conn;
    HashMap<String,String> hashMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credential);
        connect = (Button)findViewById(R.id.connect);
        credential = findViewById(R.id.email1_connect);
        conn = (TextView)findViewById(R.id.connection);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    conn();
                    write();
                    checkConnection(Temp);

                }
                else {

                    Toast.makeText(Credential.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    private void conn() {
        Temp = credential.getText().toString();


    }
    public void printMessage(String m) {
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    public void write()
    {

        try {
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            String data = Temp = credential.getText().toString();
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            Toast.makeText(Credential.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        printMessage("writing to file " + filename + "completed...");



    }

    private void act(){
        credential.setText("");
        conn.setText("Connection Succesful");
        conn.setVisibility(View.VISIBLE);
        Intent intent9 = new Intent(Credential.this, MainActivity1.class);
        intent9.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent9);
        finish();
        Toast.makeText(getBaseContext(), "Welcome To MEENA ELECTRICAL AND ENGINEERING WORKS", Toast.LENGTH_SHORT).show();
    }

    public void CheckEditTextIsEmptyOrNot(){
        Temp = credential.getText().toString();

        CheckEditText = !TextUtils.isEmpty(Temp);
    }



    public void checkConnection(final String check){

        class UserLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Credential.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if(httpResponseMsg.equalsIgnoreCase("Data Matched")){

                    //finish();
                    act();

                    Toast.makeText(Credential.this,httpResponseMsg,Toast.LENGTH_LONG).show();

                }
                else{
                    conn.setText("Please Check your Connection Address");
                    conn.setVisibility(View.VISIBLE);

                    Toast.makeText(Credential.this,httpResponseMsg,Toast.LENGTH_LONG).show();

                }

            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("check",params[0]);



                finalResult = httpParse.postRequest(hashMap, credential.getText().toString()+"/android/check_connection.php");

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(check);
    }
    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
           finish();
        }
        pressedTime = System.currentTimeMillis();
    }



}