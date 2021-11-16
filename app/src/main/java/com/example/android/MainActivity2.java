package com.example.android;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

//import com.example.android.R;
import com.android.volley.VolleyError;
import com.example.android.http.AsyncResponse;
import com.example.android.http.HttpCall;

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = "MainActivity2";
    //String ServerURL = "http://192.168.1.106:80/Android/register_employee.php";
   // String url = "http://192.168.1.106:80/Android/register_employee.php";
    EditText fname, lname, mobile, aadhar, pan, dob, imageid, address;
    TextView supervisorid;
    Button button;
    private String UserEmail;
    private TextView ids;
    String TempName, Temp2, Temp3, Temp4, Temp5, Temp6, Temp7, Temp8, Temp9,file;
    String firstname, lastname, contact, aadharno, panno, image_id, addressno,supervisor_id;
    DatePickerDialog picker;
    private long pressedTime;
    private final String filename2 = "address.txt";



    //  @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        fname = (EditText) findViewById(R.id.editText2);
        lname = (EditText) findViewById(R.id.editText3);
        mobile = (EditText) findViewById(R.id.editText4);
        aadhar = (EditText) findViewById(R.id.editText5);
        imageid = (EditText) findViewById(R.id.editText8);
        pan = (EditText) findViewById(R.id.editText6);
        dob = (EditText) findViewById(R.id.editText7);
        address = (EditText) findViewById(R.id.editaddress);
        supervisorid = (TextView) findViewById(R.id.editText98);
        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");
        supervisorid.setText(str);
        ids = findViewById(R.id.ids);
        button = (Button) findViewById(R.id.button);
        readData();

        button.setOnClickListener(view -> {
            readFromUI();

            insertData(TempName, Temp2, Temp3, Temp4, Temp5, Temp6, Temp7, Temp8, Temp9);

        });


        dob.setInputType(InputType.TYPE_NULL);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity2.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dob.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }


    public void readFromUI() {

        TempName = fname.getText().toString();

        Temp2 = lname.getText().toString();

        Temp3 = mobile.getText().toString();

        Temp4 = aadhar.getText().toString();

        Temp5 = pan.getText().toString();

        Temp6 = dob.getText().toString();

        Temp7 = imageid.getText().toString();

        Temp8 = address.getText().toString();

        Temp9 = supervisorid.getText().toString();

    }

    public void insertData(final String fname, final String lname, final String mobile, final String aadhar, final String pan, final String dob, final String imageid, final String address, final String supervisor) {
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("fname", fname);
        requestBodyMap.put("lname", lname);
        requestBodyMap.put("mobile", mobile);
        requestBodyMap.put("address", address);
        requestBodyMap.put("aadhar", aadhar);
        requestBodyMap.put("pan", pan);
        requestBodyMap.put("dob", dob);
        requestBodyMap.put("imageid", imageid);
        requestBodyMap.put("supervisor", supervisor);

        HttpCall.makeFormRequest(this, file+"/Android/register_employee.php", requestBodyMap, new AsyncResponse() {
            @Override
            public void postExecute(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject bodyObject = jsonObject.getJSONObject("body");
                    String id = bodyObject.getString("id");
                    String guid = bodyObject.getString("guid");

                    Intent intent = new Intent(getApplicationContext(), FingerPrintStoreActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("guid",guid);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void postError(VolleyError error) {

            }
        });
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        //String myStrin = savedInstanceState.getString("MyString");
        String lastname =savedInstanceState.getString("lname");
        String aadharno =savedInstanceState.getString("aadhar", aadhar.toString());
        String image_id =savedInstanceState.getString("image", imageid.toString());
        String panno =savedInstanceState.getString("pan", pan.toString());
        String addressno =savedInstanceState.getString("address", address.toString());
        String firstname =savedInstanceState.getString("fname", fname.toString());
        String supervisor_id =savedInstanceState.getString("supervisor", supervisorid.toString());
      //  String myString =savedInstanceState.getString("MyString", "Welcome back to Android");
        lname.setText(lastname);
    }

    @Override
     protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("fname", TempName);
        outState.putString("lname", Temp2);
        outState.putString("aadhar", Temp4);
        outState.putString("image", Temp7);
        outState.putString("pan", Temp5);
        outState.putString("address", Temp8);
        outState.putString("supervisor", Temp9);

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
        printMessage("reading to file " + filename2 + " completed..");
    }


    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Intent intent = new Intent(MainActivity2.this, MainActivity1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}
