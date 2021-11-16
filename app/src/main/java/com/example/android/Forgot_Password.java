package com.example.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class Forgot_Password extends AppCompatActivity {
    EditText old, newp, renewp;
    Button set;
    private long pressedTime;
    private final String filename2 = "address.txt";
    String file,np,rp,an;
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String finalResult ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        old = (EditText) findViewById(R.id.reset1);
        newp = (EditText) findViewById(R.id.reset2);
        renewp = (EditText) findViewById(R.id.reset3);
        set = (Button) findViewById(R.id.reset);
        readData();


        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    UserLoginFunction(an,rp);


                }
                else {

                    Toast.makeText(Forgot_Password.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }

            }
        });




    }

    public void CheckEditTextIsEmptyOrNot(){

        an = old.getText().toString();
        np = newp.getText().toString();
        rp = renewp.getText().toString();
        CheckEditText = !TextUtils.isEmpty(an) && !TextUtils.isEmpty(np) && !TextUtils.isEmpty(rp) && TextUtils.equals(np,rp);
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
    }




    public void UserLoginFunction(final String aadhar, final String rpassword){

        class UserLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Forgot_Password.this,"Loading Data",null,true,false);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if(httpResponseMsg.equalsIgnoreCase("Data Matched")){

                    finish();

                    Intent intent = new Intent(Forgot_Password.this, Employee_Login.class);

                    //intent.putExtra(UserEmail,email);
                   // intent.putExtra("message_key",EmailHolder);
                    Toast.makeText(Forgot_Password.this, "Leave Form", Toast.LENGTH_SHORT).show();
                    //intent.putExtra("message_key",EmailHolder);

                    startActivity(intent);

                }
                else{

                    Toast.makeText(Forgot_Password.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("aadhar",params[0]);

                hashMap.put("rpassword",params[1]);

                finalResult = httpParse.postRequest(hashMap, file+"/android/password_reset.php");

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(aadhar,rpassword);
    }


    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Intent intent = new Intent(Forgot_Password.this, Custom_Action.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }


}