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

public class Leave extends AppCompatActivity {
    private static final String TAG = "Leave";
    //String ServerURL = "http://192.168.1.106:80/Android/register_employee.php";
    // String url = "http://192.168.1.106:80/Android/register_employee.php";
    EditText name,empid,start,end,reason;
    TextView status;
    Button button;
    private String UserEmail;
    private TextView ids;
    String TempName, Temp2, Temp3, Temp4, Temp5, Temp6,file;
    String firstname, lastname, contact, aadharno, panno, image_id, addressno,supervisor_id;
    DatePickerDialog picker,picker2;
    private long pressedTime;
    private final String filename2 = "address.txt";



    //  @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaveemployee);

        name = (EditText) findViewById(R.id.leave_name);
        empid = (EditText) findViewById(R.id.leave_id);
        start = (EditText) findViewById(R.id.leave_date);
        end = (EditText) findViewById(R.id.leave_date2);
        reason = (EditText) findViewById(R.id.leave_reason);
        button = (Button) findViewById(R.id.leave_button47);
        status = (TextView) findViewById(R.id.leave_status);
        readData();

        button.setOnClickListener(view -> {
            readFromUI();

            insertData(TempName, Temp2, Temp3, Temp4, Temp5, Temp6);

        });


        start.setInputType(InputType.TYPE_NULL);
        start.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(Leave.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            start.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }, year, month, day);
            picker.show();
        });

        end.setInputType(InputType.TYPE_NULL);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day1 = cldr.get(Calendar.DAY_OF_MONTH);
                int month1 = cldr.get(Calendar.MONTH);
                int year1 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker2 = new DatePickerDialog(Leave.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                end.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year1, month1, day1);
                picker2.show();
            }
        });




    }


    public void readFromUI() {

        TempName = name.getText().toString();

        Temp2 = empid.getText().toString();

        Temp3 = start.getText().toString();

        Temp4 = end.getText().toString();

        Temp5 = reason.getText().toString();

        Temp6 = status.getText().toString();

    }

    public void insertData(final String name, final String empid, final String start, final String end, final String reason, final String status){
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("name", name);
        requestBodyMap.put("empid", empid);
        requestBodyMap.put("start", start);
        requestBodyMap.put("end", end);
        requestBodyMap.put("reason", reason);
        requestBodyMap.put("status", status);

        HttpCall.makeFormRequest(this, file+"/Android/Leave.php", requestBodyMap, new AsyncResponse() {
            @Override
            public void postExecute(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject bodyObject = jsonObject.getJSONObject("body");
                    String id = bodyObject.getString("id");
                    String guid = bodyObject.getString("guid");

                    Intent intent = new Intent(Leave.this, Custom_Action.class);
                    intent.putExtra("id",id);
                    intent.putExtra("guid",guid);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "error2", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void postError(VolleyError error) {
                Toast.makeText(getBaseContext(), "error3", Toast.LENGTH_SHORT).show();


            }
        });
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
      //  printMessage("reading to file " + filename2 + " completed..");
    }


    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Intent intent = new Intent(Leave.this, Custom_Action.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}
