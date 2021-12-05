package com.example.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.android.volley.VolleyError;
import com.example.android.http.AsyncResponse;
import com.example.android.http.HttpCall;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mark_attendance extends AppCompatActivity implements MFS100Event,PopupMenu.OnMenuItemClickListener {
//public class mark_attendance extends AppCompatActivity implements MFS100Event {

    EditText Email, Password;
    Button FINGERSTRING;
    EditText idBox;
    Button LogIn;
    String PasswordHolder, EmailHolder, FINGERSTRINGHolder;
    String finalResult;
    //String HttpURL = "http://192.168.1.106:80/android/finger_check.php";
    // private static final String URL_PRODUCTS = "http://192.168.1.106:80/android/get_all.php";
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    //public static final String UserEmail = "";
    public static final String userFinger = "";
    FusedLocationProviderClient mFusedLocationClient;
    TextView latitudeTextView, longitTextView, text_card, text_card_contact;
    int PERMISSION_ID = 44;
    String latitude, longitude, file;
    List<Biometric> allFingers = new ArrayList<>();
    private String[] fingerprints;
    MFS100 mfs100 = null;
    private long pressedTime;
    boolean isCaptureRunning = true;
    private long mLastAttTime = 0l;
    private FingerData lastCapFingerData = null;
    Context context;
    private static final long Threshold = 1500;
    Biometric biometric;
    private String e_id;
    private final String filename2 = "address.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mark_attendance);
        Intent intent = getIntent();
        e_id = intent.getStringExtra("id");
        readData();


        FINGERSTRING = findViewById(R.id.fingerstring);
        LogIn = findViewById(R.id.Login5);
        idBox = findViewById(R.id.idBox);
        idBox.setText(e_id);
        CardView cardView = (CardView) findViewById(R.id.card_view99);
        cardView.setRadius(20F);
        text_card = findViewById(R.id.text_card);
        text_card_contact = findViewById(R.id.text_card2);


        latitudeTextView = findViewById(R.id.received_value_id);
        longitTextView = findViewById(R.id.received_value_id2);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // method to get the location
        mFusedLocationClient.getLastLocation();

        latitude = latitudeTextView.getText().toString();
        longitude = longitTextView.getText().toString();
        fingerprints = new String[10];



        LogIn.setOnClickListener(new View.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(mark_attendance.this, view);
                popup.setOnMenuItemClickListener(mark_attendance.this);
                popup.inflate(R.menu.popup_menu);
                popup.show();
            }


        });





//        LogIn.setOnClickListener(new View.OnClickListener() {
//
//
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onClick(View view) {
//                StartSyncCapture();
//                while (lastCapFingerData == null) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                for (String fingerprint : biometric.getFingerprints()) {
//                    String cleanedFp = cleanFP(fingerprint);
//                    if (mfs100.MatchISO(lastCapFingerData.ISOTemplate(), Base64.getDecoder().decode(cleanedFp)) > 90) {
//                        markAttendance(biometric.getId());
//                        break;
//                    }
//                }
//                lastCapFingerData = null;
//            }
//
//
//        });


        FINGERSTRING.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> reqMap = new HashMap<>();
                reqMap.put("empId", idBox.getText().toString());
                HttpCall.makeFormRequest(mark_attendance.this, file + "/Android/get_all.php", reqMap, new AsyncResponse() {
                    @Override
                    public void postExecute(String response) {
                        biometric = new Gson().fromJson(response, Biometric.class);
                        String a[] = biometric.updateArray();
                        biometric.updateArray();
                        // loadfingers();
                        idBox.setClickable(false);
                        idBox.setFocusable(false);
                        text_card.setText(biometric.getFirstname());
                        text_card_contact.setText(biometric.getContact_info());
                        cardView.setVisibility(View.VISIBLE);
                        FINGERSTRING.setVisibility(View.GONE);
                        Log.i("TAG", "postExecute: ");
                    }

                    @Override
                    public void postError(VolleyError error) {

                    }
                });
            }
        });

    }

    private String cleanFP(String fingerprint) {
        if(fingerprint == null || fingerprint.length() < 10){
            return "";
        }
        fingerprint = fingerprint.replaceAll("\n", "");
        int sInd = fingerprint.indexOf('#');
        int eInd = fingerprint.lastIndexOf('#');
        if(sInd < 0 || eInd < 0)
            return fingerprint;
        return fingerprint.substring(sInd + 1, eInd);
    }

    private void markAttendance(String ids) {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("ids", ids);
        requestMap.put("lat", latitudeTextView.getText().toString());
        requestMap.put("longi", longitTextView.getText().toString());
        HttpCall.makeFormRequest(this, file + "/android/finger_check.php", requestMap, new AsyncResponse() {
            @Override
            public void postExecute(String response) {
                Log.i("TAG", "postExecute: " + response);
            }

            @Override
            public void postError(VolleyError error) {

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
//                        Location location = new Location("");
//                        location.setLatitude(23.23);
//                        location.setLongitude(23.24);
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
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                               @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    public void CheckEditTextIsEmptyOrNot() {

        //  EmailHolder = Email.getText().toString();
        PasswordHolder = latitudeTextView.getText().toString();
        FINGERSTRINGHolder = FINGERSTRING.getText().toString();

        CheckEditText = !TextUtils.isEmpty(FINGERSTRINGHolder);
    }


//    private void loadfingers() {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PRODUCTS,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            //converting the string to json array object
//                            //JSONObject obj = new JSONObject(response);
//                            JSONArray array = new JSONArray(response);
//
//                            //traversing through all the object
//                            for (int i = 0; i < array.length(); i++) {
//
//                                //getting product object from json array
//                                JSONObject biomatric = array.getJSONObject(i);
//
//                                //adding the product to product list
//                                allFingers.add(new Biometric(
//                                        biomatric.getString("image_name"),
//                                        biomatric.getString("fingerprint"),
//                                        biomatric.getString("fingerprint2"),
//                                        biomatric.getString("fingerprint3"),
//                                        biomatric.getString("fingerprint4"),
//                                        biomatric.getString("fingerprint5"),
//                                        biomatric.getString("fingerprint6"),
//                                        biomatric.getString("fingerprint7"),
//                                        biomatric.getString("fingerprint8"),
//                                        biomatric.getString("fingerprint9"),
//                                        biomatric.getString("fingerprint10"),
//                                        biomatric.getString("firstname"),
//                                        biomatric.getString("lname"),
//                                        biomatric.getString("address"),
//                                        biomatric.getString("aadhar"),
//                                        biomatric.getString("birth"),
//                                        biomatric.getString("contact")
//
//                                ));
//                            }
//
//                            Log.i("TAG", "onResponse: " + allFingers.size());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        //  matchAndMark();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//        //adding our stringrequest to queue
//        Volley.newRequestQueue(this).add(stringRequest);
//    }

//    private void matchAndMark() {
//        //StartSyncCapture(fingerprints, 0);
//        //String inputFinger = FINGERSTRING.getText();
//        for (int i = 0; i < allFingers.size(); i++) {
//            Biomatric biomatric = allFingers.get(i);
//            for (int j = 0; j < 10; j++) {
//                boolean isMatched = VerifyFinger(fingerprints[0], (Biomatric.getFingerprints()[j]));
//                if (isMatched) {
//                    UserLoginFunction(FINGERSTRINGHolder,Biomatric.getImage_name(),latitude, longitude);
//                }
//
//            }
//        }
//    }


//    public void UserLoginFunction(final String Fg, final String lat, final String longi) {
//
//        class UserLoginClass extends AsyncTask<String, Void, String> {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//
//                progressDialog = ProgressDialog.show(mark_attendance.this, "Loading Data", null, true, true);
//            }
//
//            @Override
//            protected void onPostExecute(String httpResponseMsg) {
//
//                super.onPostExecute(httpResponseMsg);
//
//                progressDialog.dismiss();
//
//                if (httpResponseMsg.equalsIgnoreCase("Data Matched new")) {
//
//                    finish();
//                    List<Biometric> allFingers = new ArrayList<>();
//                    loadfingers();
//
//                    Intent intent = new Intent(mark_attendance.this, mark_attendance_dashboard.class);
//
//
//                    //intent.putExtra(UserEmail,email);
//                    intent.putExtra(userFinger, Fg);
//                    startActivity(intent);
//
//                }
//                if (httpResponseMsg.equalsIgnoreCase("Data updated")) {
//
//                    finish();
//                    List<Biometric> allFingers = new ArrayList<>();
//                    loadfingers();
//
//                    Intent intent = new Intent(mark_attendance.this, mark_attendance_dashboard.class);
//
//
//                    //intent.putExtra(UserEmail,email);
//                    intent.putExtra(userFinger, Fg);
//                    startActivity(intent);
//
//                } else {
//
//                    Toast.makeText(mark_attendance.this, httpResponseMsg, Toast.LENGTH_LONG).show();
//                }
//
//            }
//
//            @Override
//            protected String doInBackground(String... params) {
//
//                //  hashMap.put("email",params[0]);
//
////                hashMap.put("password",params[1]);
//
//                hashMap.put("Fg", params[0]);
//                hashMap.put("lat", params[1]);
//                hashMap.put("longi", params[2]);
//
//                finalResult = httpParse.postRequest(hashMap, HttpURL);
//
//                return finalResult;
//            }
//        }
//
//        UserLoginClass userLoginClass = new UserLoginClass();
//
//        userLoginClass.execute(Fg, lat, longi);
//    }

    @Override
    protected void onStart() {
        try {
            if (mfs100 == null) {
                mfs100 = new MFS100((MFS100Event) this);
                mfs100.SetApplicationContext(mark_attendance.this);
            } else {
                InitScanner();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStart();

    }

    @Override
    public void OnDeviceAttached(int vid, int pid, boolean hasPermission) {

        if (SystemClock.elapsedRealtime() - mLastAttTime < Threshold) {
            return;
        }
        mLastAttTime = SystemClock.elapsedRealtime();
        int ret;
        if (!hasPermission) {
            SetTextOnUIThread("Permission denied");
            return;
        }
        try {
            if (vid == 1204 || vid == 11279) {
                if (pid == 34323) {
                    ret = mfs100.LoadFirmware();
                    if (ret != 0) {
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    } else {
                        SetTextOnUIThread("Load firmware success");
                    }
                } else if (pid == 4101) {
                    String key = "Without Key";
                    ret = mfs100.Init();
                    if (ret == 0) {
                        showSuccessLog(key);
                    } else {
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetTextOnUIThread(final String str) {
        Log.i("TAG", "SetTextOnUIThread: " + str);
    }

    private void SetLogOnUIThread(final String str) {

        Log.i("TAG", "SetLogOnUIThread: " + str);
    }

    private void showSuccessLog(String key) {
        try {
            Log.i("TAG", "showSuccessLog: Scanner Initialized");
            String info = "\nKey: " + key + "\nSerial: "
                    + mfs100.GetDeviceInfo().SerialNo() + " Make: "
                    + mfs100.GetDeviceInfo().Make() + " Model: "
                    + mfs100.GetDeviceInfo().Model()
                    + "\nCertificate: " + mfs100.GetCertification();
        } catch (Exception e) {
        }
    }

    long mLastDttTime = 0l;

    @Override
    public void OnDeviceDetached() {
        try {

            if (SystemClock.elapsedRealtime() - mLastDttTime < Threshold) {
                return;
            }
            mLastDttTime = SystemClock.elapsedRealtime();
            UnInitScanner();

            SetTextOnUIThread("Device removed");
        } catch (Exception e) {
        }
    }

    private void UnInitScanner() {
        try {
            int ret = mfs100.UnInit();
            if (ret != 0) {
                SetTextOnUIThread(mfs100.GetErrorMsg(ret));
            } else {
                SetLogOnUIThread("Uninit Success");
                SetTextOnUIThread("Uninit Success");
                lastCapFingerData = null;
            }
        } catch (Exception e) {
            Log.e("UnInitScanner.EX", e.toString());
        }
    }

    @Override
    public void OnHostCheckFailed(String err) {
        try {
            SetLogOnUIThread(err);
            Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
        } catch (Exception ignored) {
        }
    }

    private void InitScanner() {
        try {
            int ret = mfs100.Init();
            if (ret != 0) {
                SetTextOnUIThread(mfs100.GetErrorMsg(ret));
            } else {
                SetTextOnUIThread("Init success");
                String info = "Serial: " + mfs100.GetDeviceInfo().SerialNo()
                        + " Make: " + mfs100.GetDeviceInfo().Make()
                        + " Model: " + mfs100.GetDeviceInfo().Model()
                        + "\nCertificate: " + mfs100.GetCertification();
                SetLogOnUIThread(info);
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Init failed, unhandled exception",
                    Toast.LENGTH_LONG).show();
            SetTextOnUIThread("Init failed, unhandled exception");
        }
    }

    private void StartSyncCapture() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SetTextOnUIThread("");
                isCaptureRunning = true;
                try {
                    FingerData fingerData = new FingerData();
                    int ret = mfs100.AutoCapture(fingerData, 10000, true);
                    Log.e("StartSyncCapture.RET", "" + ret);
                    if (ret != 0) {
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    } else {
                        lastCapFingerData = fingerData;
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(fingerData.FingerImage(), 0,
                                fingerData.FingerImage().length);
                        mark_attendance.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Runnable runnable = () -> {
                                    for (String fingerprint : biometric.getFingerprints()) {
                                        String cleanedFp = cleanFP(fingerprint);
                                        if (mfs100.MatchISO(lastCapFingerData.ISOTemplate(), Base64.getDecoder().decode(cleanedFp)) > 90) {
                                            markAttendance(biometric.getId());
                                            break;
                                        }
                                    }
                                    lastCapFingerData = null;
                                };
                                new Thread(runnable).start();
                            }
                        });

//                        Log.e("RawImage", Base64.encodeToString(fingerData.RawData(), Base64.DEFAULT));
//                        Log.e("FingerISOTemplate", Base64.encodeToString(fingerData.ISOTemplate(), Base64.DEFAULT));
                        SetTextOnUIThread("Capture Success");
                        String log = "\nQuality: " + fingerData.Quality()
                                + "\nNFIQ: " + fingerData.Nfiq()
                                + "\nWSQ Compress Ratio: "
                                + fingerData.WSQCompressRatio()
                                + "\nImage Dimensions (inch): "
                                + fingerData.InWidth() + "\" X "
                                + fingerData.InHeight() + "\""
                                + "\nImage Area (inch): " + fingerData.InArea()
                                + "\"" + "\nResolution (dpi/ppi): "
                                + fingerData.Resolution() + "\nGray Scale: "
                                + fingerData.GrayScale() + "\nBits Per Pixal: "
                                + fingerData.Bpp() + "\nWSQ Info: "
                                + fingerData.WSQInfo();
                        SetLogOnUIThread(log);
                        //SetData2(fingerData);
                    }
                } catch (Exception ex) {
                    SetTextOnUIThread("Error");
                } finally {
                    isCaptureRunning = false;
                }
            }
        }).start();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
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
       // printMessage("reading to file " + filename2 + " completed..");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(this, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.search_item:
                StartSyncCapture();
                return true;
            case R.id.upload_item:
                // do your code
                return true;
            case R.id.copy_item:
                Intent intent = new Intent(mark_attendance.this, Nfc.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }



    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            if (checkPermissions()) {
                getLastLocation();

                Intent intent = new Intent(mark_attendance.this, MainActivity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(mark_attendance.this, MainActivity1.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

}