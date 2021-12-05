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

public class ValidateIDCard extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
//public class mark_attendance extends AppCompatActivity implements MFS100Event {

    EditText Email, Password;
    Button FINGERSTRING;
    EditText idBox;
    Button LogIn, Logg;
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
    String latitude, longitude, file, ids;
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

        readData();


        FINGERSTRING = findViewById(R.id.fingerstring);
        LogIn = findViewById(R.id.Login56);
        Logg = findViewById(R.id.Login5);
        Logg.setVisibility(View.GONE);
        LogIn.setVisibility(View.VISIBLE);
        idBox = findViewById(R.id.idBox);
        idBox.setText(e_id);
        CardView cardView = (CardView) findViewById(R.id.card_view99);
        cardView.setRadius(20F);
        text_card = findViewById(R.id.text_card);
        text_card_contact = findViewById(R.id.text_card2);





        LogIn.setOnClickListener(new View.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(ValidateIDCard.this, view);
                popup.setOnMenuItemClickListener(ValidateIDCard.this);
                popup.inflate(R.menu.popup_menu);
                popup.show();
            }


        });


        FINGERSTRING.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> reqMap = new HashMap<>();
                reqMap.put("empId", idBox.getText().toString());
                HttpCall.makeFormRequest(ValidateIDCard.this, file + "/Android/get_all.php", reqMap, new AsyncResponse() {
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
                        ids = biometric.getId();
                        Log.i("TAG", "postExecute: ");
                    }

                    @Override
                    public void postError(VolleyError error) {

                    }
                });
            }
        });

    }









    public void CheckEditTextIsEmptyOrNot() {

        //  EmailHolder = Email.getText().toString();

        FINGERSTRINGHolder = FINGERSTRING.getText().toString();

        CheckEditText = !TextUtils.isEmpty(FINGERSTRINGHolder);
    }




    @Override
    public void onResume() {
        super.onResume();
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

            case R.id.copy_item:
                Intent intent = new Intent(ValidateIDCard.this, NfcWrite.class);
                intent.putExtra("message_key",ids);
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
            {

                Intent intent = new Intent(ValidateIDCard.this, Admin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(ValidateIDCard.this, MainActivity1.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

}
