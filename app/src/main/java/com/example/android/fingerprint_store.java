package com.example.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.android.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class fingerprint_store extends AppCompatActivity  {
    //implements View.OnClickListener
    String ServerURL = "http://192.168.1.106:80/Android/fingerprint_store.php" ;
    EditText lat, longi,fingerprint;
    TextView imageid ;
    Button button;
    Button button2;
    String TempName, Temp2, Temp3,Temp7;
    DatePickerDialog picker;
    TextView receiver_msg;
    FusedLocationProviderClient mFusedLocationClient;
    TextView latitudeTextView, longitTextView;
    int PERMISSION_ID = 44;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerprint_store);

//        lat = (EditText) findViewById(R.id.editText2);
//        longi = (EditText) findViewById(R.id.editText3);
        fingerprint = (EditText) findViewById(R.id.editText4);


        imageid = (TextView) findViewById(R.id.editText5);
        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");
        imageid.setText(str);
        latitudeTextView = findViewById(R.id.received_value_id);
        longitTextView = findViewById(R.id.received_value_id2);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        getLastLocation();






            button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                GetData();

                InsertData(TempName, Temp2, Temp3,Temp7);

            }
        });



        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(fingerprint_store.this, button2);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.one:
                                Toast.makeText(fingerprint_store.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.two:
                                Toast.makeText(fingerprint_store.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.three:
                                Toast.makeText(fingerprint_store.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.four:
                                Toast.makeText(fingerprint_store.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.five:
                                Toast.makeText(fingerprint_store.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.six:
                                Toast.makeText(fingerprint_store.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.seven:
                                Toast.makeText(fingerprint_store.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.eight:
                                Toast.makeText(fingerprint_store.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.nine:
                                Toast.makeText(fingerprint_store.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.ten:
                                Toast.makeText(fingerprint_store.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                return true;

                            default:
                                return true;

                        }

                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method
    }





    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitudeTextView.setText(location.getLatitude() + "");
                            longitTextView.setText(location.getLongitude() + "");

                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }


    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");



        }



    };


    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }




    public void GetData(){


        TempName = latitudeTextView.getText().toString();

        Temp2 = longitTextView.getText().toString();

        Temp3 = fingerprint.getText().toString();

        Temp7 = imageid.getText().toString();

    }

    public void InsertData(final String lat, final String longi,final String fingerprint,final String imageid){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = latitudeTextView.getText().toString() ;

                String EmailHolder = longitTextView.getText().toString() ;
                String EmailHolder2 = fingerprint ;

                String EmailHolder6 = imageid ;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("lat", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("longi", EmailHolder));
                nameValuePairs.add(new BasicNameValuePair("fingerprint", EmailHolder2));

                nameValuePairs.add(new BasicNameValuePair("imageid", EmailHolder6));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);


                Toast.makeText(fingerprint_store.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(fingerprint_store.this,MainActivity3.class);
                startActivity(intent);
                intent.setFlags(intent.FLAG_ACTIVITY_NO_HISTORY);

                finish();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(lat, longi, fingerprint,imageid);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

}
