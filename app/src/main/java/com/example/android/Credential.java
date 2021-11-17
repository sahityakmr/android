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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileOutputStream;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;

import com.android.volley.VolleyError;
import com.example.android.http.AsyncResponse;
import com.example.android.http.HttpCall;
import com.google.android.material.navigation.NavigationView;
import com.nightonke.boommenu.BoomMenuButton;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;


public class Credential extends AppCompatActivity {

    Button connect;
    EditText credential;
    ImageView imageView;
    String Temp;
    Boolean CheckEditText ;
    private final String filename = "address.txt";
    ProgressDialog progressDialog;
    HttpParse httpParse = new HttpParse();
    private long pressedTime;
    String finalResult ;
    TextView conn;
    CallbackManager callbackManager;
    HashMap<String,String> hashMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credential);
        connect = (Button)findViewById(R.id.connect);
        credential = findViewById(R.id.email1_connect);
        conn = (TextView)findViewById(R.id.connection);
        callbackManager = CallbackManager.Factory.create();
        imageView = (ImageView) findViewById(R.id.imageView);
        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email","public_profile");

//        File address = new File(getApplicationContext().deleteFile("address.txt"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                        File address = new File(getApplicationContext().getFilesDir(),"address.txt");
                        address.delete();
                        Intent intent = new Intent(Credential.this, MainActivity1.class);
                        intent.setFlags(intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();

                }

            @Override
            public void onCancel() {
                Intent intent = new Intent(Credential.this, splash.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    AccessTokenTracker tokentracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){
                Toast.makeText(Credential.this, "User Logged Out", Toast.LENGTH_SHORT).show();

            }else{
                loadUserProfile(currentAccessToken);

            }
        }
    };

    private void loadUserProfile(AccessToken newAccessToken)
    {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(@Nullable JSONObject object, @Nullable GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String image_url = "https://graph.facebook.com/"+id+ "/picture?type=normal";
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //  Bundle parameters = new Bundle();
        String[] names = new String[]{"fields", "first_name", "last_name", "email", "id"};
        // parameters.putString(Arrays.toString(names));
        //request.setParameters(parameters);
        //request.executeAsync();
    }

    private void checkLoginStatus()
    {
        if(AccessToken.getCurrentAccessToken()!=null)
        {
            loadUserProfile(AccessToken.getCurrentAccessToken());


        }
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        AccessTokenTracker.stopTracking();
//    }

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