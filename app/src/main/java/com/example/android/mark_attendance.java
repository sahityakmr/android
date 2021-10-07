package com.example.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;

public class mark_attendance extends AppCompatActivity {

    EditText Email, Password,FINGERSTRING;
    Button LogIn ;
    String PasswordHolder, EmailHolder, FINGERSTRINGHolder;
    String finalResult ;
    String HttpURL = "http://192.168.1.106:80/android/finger_check.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    //public static final String UserEmail = "";
    public static final String userFinger = "";
    FusedLocationProviderClient mFusedLocationClient;
    TextView latitudeTextView, longitTextView;
    int PERMISSION_ID = 44;
    String latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mark_attendance);

      //  Email = (EditText)findViewById(R.id.email);
      //  Password = (EditText)findViewById(R.id.password);
        FINGERSTRING = (EditText)findViewById(R.id.fingerstring);
        LogIn = (Button)findViewById(R.id.Login);


        latitudeTextView = findViewById(R.id.received_value_id);
        longitTextView = findViewById(R.id.received_value_id2);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // method to get the location
        getLastLocation();

        latitude= latitudeTextView.getText().toString();
        longitude = longitTextView.getText().toString();

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    UserLoginFunction(FINGERSTRINGHolder,latitude,longitude);

                }
                else {

                    Toast.makeText(mark_attendance.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }

            }
        });
    }



    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
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
            // if permissions aren't available,
            // request for permissions
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

    // method to check for permissions
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

















    public void CheckEditTextIsEmptyOrNot(){

      //  EmailHolder = Email.getText().toString();
        PasswordHolder = latitudeTextView.getText().toString();
        FINGERSTRINGHolder = FINGERSTRING.getText().toString();

        if(TextUtils.isEmpty(FINGERSTRINGHolder))
        {
            CheckEditText = false;
        }
        else {

            CheckEditText = true ;
        }
    }

    public void UserLoginFunction(final String Fg, final String lat, final String longi){

        class UserLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(mark_attendance.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if(httpResponseMsg.equalsIgnoreCase("Data Matched new"))
                {

                    finish();

                    Intent intent = new Intent(mark_attendance.this, mark_attendance_dashboard.class);

                    //intent.putExtra(UserEmail,email);
                    intent.putExtra(userFinger,Fg);
                    startActivity(intent);

                }
                if(httpResponseMsg.equalsIgnoreCase("Data updated"))
                {

                    finish();

                    Intent intent = new Intent(mark_attendance.this, mark_attendance_dashboard.class);

                    //intent.putExtra(UserEmail,email);
                    intent.putExtra(userFinger,Fg);
                    startActivity(intent);

                }

                else{

                    Toast.makeText(mark_attendance.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

              //  hashMap.put("email",params[0]);

//                hashMap.put("password",params[1]);

                hashMap.put("Fg",params[0]);
                hashMap.put("lat",params[1]);
                hashMap.put("longi",params[2]);
                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(Fg,lat,longi);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }


}