package com.example.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
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
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;

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
import java.util.List;

public class FingerPrintStoreActivity extends AppCompatActivity implements MFS100Event {
    private static final String TAG = "FingerPrintStore";
    private static long Threshold = 1500;
    String ServerURL = "http://192.168.1.106:80/Android/fingerprint_store.php";
    EditText FINGERSTRING, fingure1, fingure2, fingure3, fingure4, fingure5, fingure6, fingure7, fingure8, fingure9, fingure10;
    TextView imageid;
    Button button;
    Button button4, button5, button6, button7, button8, button9, button10, button11, button12, button13, button14;
    String TempName, Temp2, Temp3, Temp7, Tempf1, Tempf2, Tempf3, Tempf4, Tempf5, Tempf6, Tempf7, Tempf8, Tempf9, Tempf10;
    DatePickerDialog picker;
    TextView receiver_msg;
    FusedLocationProviderClient mFusedLocationClient;
    TextView latitudeTextView, longitTextView;
    int PERMISSION_ID = 44;
    TextView fileContent;
    TextView name;
    SharedPreferences SharedPreferences;
    Context context;
    MFS100 mfs100 = null;
    boolean isCaptureRunning = true;
    private long mLastAttTime=0l;
    private FingerData lastCapFingerData = null;

    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerprint_store);


        imageid = (TextView) findViewById(R.id.imageid);

        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");
        imageid.setText(str);
        fingure1 = findViewById(R.id.received_value_id3);
        fingure2 = findViewById(R.id.received_value_id4);
        fingure3 = findViewById(R.id.received_value_id5);
        fingure4 = findViewById(R.id.received_value_id6);
        fingure5 = findViewById(R.id.received_value_id7);
        fingure6 = findViewById(R.id.received_value_id8);
        fingure7 = findViewById(R.id.received_value_id9);
        fingure8 = findViewById(R.id.received_value_id10);
        fingure9 = findViewById(R.id.received_value_id11);
        fingure10 = findViewById(R.id.received_value_id12);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button9);
        button9 = (Button) findViewById(R.id.button10);
        button10 = (Button) findViewById(R.id.button11);
        button11 = (Button) findViewById(R.id.button12);
        button12 = (Button) findViewById(R.id.button13);
        button13 = (Button) findViewById(R.id.button14);



        latitudeTextView = findViewById(R.id.received_value_id);
        longitTextView = findViewById(R.id.received_value_id2);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        getLastLocation();


        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                GetData();
                fin1();
                fin2();
                fin3();
                fin4();
                fin5();
                fin6();
                fin7();
                fin8();
                fin9();
                fin10();

                InsertData(TempName, Temp2, Tempf1, Tempf2, Tempf3, Tempf4, Tempf5, Tempf6, Tempf7, Tempf8, Tempf9, Tempf10, Temp7);


            }
        });


        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fin1();

            }
        });


        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fin2();

            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fin3();

            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fin4();

            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fin5();

            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fin6();

            }
        });

        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fin7();

            }
        });

        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fin8();

            }
        });

        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fin9();

            }
        });

        button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fin10();

            }


        });


    }

    public void fin1() {
        // StartSyncCapture();
        Tempf1 = fingure1.getText().toString();
        Toast.makeText(FingerPrintStoreActivity.this, "You Clicked : 1", Toast.LENGTH_SHORT).show();
    }

    public void fin2() {

        Tempf2 = fingure2.getText().toString();

        Toast.makeText(FingerPrintStoreActivity.this, "You Clicked : 2", Toast.LENGTH_SHORT).show();
    }

    public void fin3() {
        Tempf3 = fingure3.getText().toString();
        Toast.makeText(FingerPrintStoreActivity.this, "You Clicked : 3", Toast.LENGTH_SHORT).show();
    }

    public void fin4() {
        Tempf4 = fingure4.getText().toString();
        Toast.makeText(FingerPrintStoreActivity.this, "You Clicked : 4", Toast.LENGTH_SHORT).show();
    }

    public void fin5() {
        Tempf5 = fingure5.getText().toString();
        Toast.makeText(FingerPrintStoreActivity.this, "You Clicked : 5", Toast.LENGTH_SHORT).show();
    }

    public void fin6() {
        Tempf6 = fingure6.getText().toString();
        Toast.makeText(FingerPrintStoreActivity.this, "You Clicked : 6", Toast.LENGTH_SHORT).show();
    }

    public void fin7() {
        Tempf7 = fingure7.getText().toString();
        Toast.makeText(FingerPrintStoreActivity.this, "You Clicked : 7", Toast.LENGTH_SHORT).show();
    }

    public void fin8() {
        Tempf8 = fingure8.getText().toString();
        Toast.makeText(FingerPrintStoreActivity.this, "You Clicked : 8", Toast.LENGTH_SHORT).show();
    }

    public void fin9() {
        Tempf9 = fingure9.getText().toString();
        Toast.makeText(FingerPrintStoreActivity.this, "You Clicked : 9", Toast.LENGTH_SHORT).show();
    }

    public void fin10() {
        Tempf10 = fingure10.getText().toString();
        Toast.makeText(FingerPrintStoreActivity.this, "You Clicked : 10", Toast.LENGTH_SHORT).show();
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
//                        Location location = new Location("");
////                        location.setLatitude(23.23);
////                        location.setLongitude(23.24);

                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitudeTextView.setText(location.getLatitude() + "");
                            longitTextView.setText(location.getLongitude() + "");

                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please " + "turn on" + " your location...", Toast.LENGTH_LONG).show();
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


    public void GetData() {


        TempName = latitudeTextView.getText().toString();

        Temp2 = longitTextView.getText().toString();


        Temp7 = imageid.getText().toString();

        //Temp3 = FINGERSTRING.getText().toString();
    }

    public void InsertData(final String lat, final String longi, final String file, final String file2, final String file3, final String file4, final String file5, final String file6, final String file7, final String file8, final String file9, final String file10, final String imageid) {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = latitudeTextView.getText().toString();

                String EmailHolder = longitTextView.getText().toString();
                String tempHolder = fingure1.getText().toString();
                String tempHolder2 = fingure2.getText().toString();
                String tempHolder3 = fingure3.getText().toString();
                String tempHolder4 = fingure4.getText().toString();
                String tempHolder5 = fingure5.getText().toString();
                String tempHolder6 = fingure6.getText().toString();
                String tempHolder7 = fingure7.getText().toString();
                String tempHolder8 = fingure8.getText().toString();
                String tempHolder9 = fingure9.getText().toString();
                String tempHolder10 = fingure10.getText().toString();


                String EmailHolder6 = imageid;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("lat", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("longi", EmailHolder));
                nameValuePairs.add(new BasicNameValuePair("file", tempHolder));
                nameValuePairs.add(new BasicNameValuePair("file2", tempHolder2));
                nameValuePairs.add(new BasicNameValuePair("file3", tempHolder3));
                nameValuePairs.add(new BasicNameValuePair("file4", tempHolder4));
                nameValuePairs.add(new BasicNameValuePair("file5", tempHolder5));
                nameValuePairs.add(new BasicNameValuePair("file6", tempHolder6));
                nameValuePairs.add(new BasicNameValuePair("file7", tempHolder7));
                nameValuePairs.add(new BasicNameValuePair("file8", tempHolder8));
                nameValuePairs.add(new BasicNameValuePair("file9", tempHolder9));
                nameValuePairs.add(new BasicNameValuePair("file10", tempHolder10));

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


                Toast.makeText(FingerPrintStoreActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(FingerPrintStoreActivity.this, MainActivity3.class);
                intent.putExtra("message_key",imageid);
                startActivity(intent);
                intent.setFlags(intent.FLAG_ACTIVITY_NO_HISTORY);

                finish();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(lat, longi, file, file2, file3, file4, file5, file6, file7, file8, file9, file10, imageid);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (checkPermissions()) {
            getLastLocation();
        }

    }

    @Override
    protected void onStart() {
        try {
            if (mfs100 == null) {
                mfs100 = new MFS100(this);
                mfs100.SetApplicationContext(FingerPrintStoreActivity.this);
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
        Log.i(TAG, "SetTextOnUIThread: " + str);
    }

    private void SetLogOnUIThread(final String str) {

        Log.i(TAG, "SetLogOnUIThread: " + str);
    }

    private void showSuccessLog(String key) {
        try {
            Log.i(TAG, "showSuccessLog: Scanner Initialized");
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
                        FingerPrintStoreActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //imgFinger.setImageBitmap(bitmap);
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
}
