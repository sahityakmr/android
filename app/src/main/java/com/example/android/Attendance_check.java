package com.example.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attendance_check extends AppCompatActivity {


    Button FINGERSTRING;
    EditText idBox,iddate;
    Button LogIn;
    String PasswordHolder, FINGERSTRINGHolder;
    //String HttpURL = "http://192.168.1.106:80/android/finger_check.php";
    //private static final String URL_PRODUCTS = "http://192.168.1.106:80/android/get_all.php";
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    TextView text_card,text_card_contact;
    int PERMISSION_ID = 44;
    String latitude, longitude;
    List<Attendance> allFingers = new ArrayList<>();
    private String[] fingerprints;
    MFS100 mfs100 = null;
    boolean isCaptureRunning = true;
    private final long mLastAttTime = 0l;
    private final FingerData lastCapFingerData = null;
    Context context;
    private static final long Threshold = 1500;
    Attendance attendance;
    private String e_id;
    private long pressedTime;
    private final String filename2 = "address.txt";
    String file;
    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_attendance);
        Intent intent = getIntent();
  //      e_id = intent.getStringExtra("id");
        readData();



   //     FINGERSTRING = findViewById(R.id.fingerstring);
   //     FINGERSTRING.setVisibility(View.GONE);
        LogIn = findViewById(R.id.Login59);
        idBox = findViewById(R.id.idBoxx);
        //idBox.setText(e_id);
        CardView cardView = (CardView) findViewById(R.id.card_view990);
        //cardView.setRadius(20F);
        text_card = findViewById(R.id.text_card1);
        text_card_contact = findViewById(R.id.text_card21);
        //fingerprints = new String[10];
        iddate = (EditText) findViewById(R.id.idDate);


       LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> reqMap = new HashMap<>();
                reqMap.put("empId", idBox.getText().toString());
                reqMap.put("iddate", iddate.getText().toString());
                HttpCall.makeFormRequest(Attendance_check.this, file+"/android/attendance_check.php", reqMap, new AsyncResponse() {
                    @Override
                    public void postExecute(String response) {
                        if (response != null) {
                            attendance = new Gson().fromJson(response, Attendance.class);
                            //attendance.updateArray();
                            // loadfingers();
                            idBox.setClickable(false);
                            idBox.setFocusable(false);
                            String[] names = new String[]{"Time IN.:-" + (attendance.gettimein()), "Time Out.:-" + (attendance.gettimeout()), "Total Hour(s).:-" + (attendance.getnumberofhour()), "Date.:-" + (attendance.getdate())};
                            //text_card.setText(biometric.getFirstname());
//                        names = new String[] {"City.:-" + city, "State.:-" + state, "Address:-" + add, "Zip:-" + zip};
                            text_card.setText(Arrays.toString(names));
                            //text_card_contact.setText(biometric.getContact_info());
                            cardView.setVisibility(View.VISIBLE);
                            LogIn.setVisibility(View.GONE);
                            Log.i("TAG", "postExecute: ");
                        } else {
                            text_card.setText("Invalid Choice Selected");
                        }
                    }
                    @Override
                    public void postError(VolleyError error) {

                    }
                });
            }
        });

        iddate.setInputType(InputType.TYPE_NULL);
        iddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Attendance_check.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                iddate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                cardView.setVisibility(View.GONE);
                LogIn.setVisibility(View.VISIBLE);
                picker.show();
            }

        });


    }

//
//    private void markAttendance(String id) {
//        Map<String, String> requestMap = new HashMap<>();
//        requestMap.put("id", id);
//        HttpCall.makeFormRequest(this, HttpURL, requestMap, new AsyncResponse() {
//            @Override
//            public void postExecute(String response) {
//                Log.i("TAG", "postExecute: " + response);
//            }
//
//            @Override
//            public void postError(VolleyError error) {
//
//            }
//        });
//    }

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
        printMessage("reading to file " + filename2 + " completed..");
    }





    @Override
    public void onBackPressed() {

    if (pressedTime + 2000 > System.currentTimeMillis()) {
        super.onBackPressed();
        finish();
    } else {
        Intent intent = new Intent(Attendance_check.this, MainActivity1.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
        Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
    }
    pressedTime = System.currentTimeMillis();
}

}