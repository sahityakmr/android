package com.example.android;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import com.android.volley.toolbox.StringRequest;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

//import com.example.android.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity  {

    String ServerURL = "http://192.168.1.106:80/Android/register_employee.php" ;
    String url = "http://192.168.1.106:80/Android/register_employee.php";
    EditText fname, lname,mobile,aadhar,pan,dob, imageid, address ;
    TextView supervisorid;
    Button button;
    private String UserEmail;
    private TextView ids;
    String TempName, Temp2,Temp3,Temp4,Temp5,Temp6,Temp7,Temp8,Temp9;
    DatePickerDialog picker;
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


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                GetData();

                InsertData(TempName, Temp2, Temp3, Temp4, Temp5, Temp6, Temp7,Temp8,Temp9);

            }
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

    public void GetData(){

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

    private void loadid() {

//        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // on below line passing our response to json object.
                            JSONObject jsonObject = new JSONObject(response);
                            // on below line we are checking if the response is null or not.
                            if (jsonObject.getString("id") == null) {
                                // displaying a toast message if we get error
                                Toast.makeText(MainActivity.this, "Please enter valid id.", Toast.LENGTH_SHORT).show();
                            } else {
                                // if we get the data then we are setting it in our text views in below line.
                                ids.setText(jsonObject.getString("id"));
                                // ids.setVisibility(View.VISIBLE);

                                // on below line we are displaying
                                // a success toast message.
                            }
                            } catch(JSONException e){
                                e.printStackTrace();
                            }
                            //get your response and parse it with Gson
                        }
                    },new Response.ErrorListener()

                    {
                        @Override
                        public void onErrorResponse (VolleyError error){
                        Toast.makeText(MainActivity.this, "Fail to get id" + error, Toast.LENGTH_SHORT).show();

                    }
                        // Something went wrong

                });

// Add the request to the RequestQueue.
       // queue.add(stringRequest);
        Volley.newRequestQueue(this).add(stringRequest);
    }




    public void InsertData(final String fname, final String lname,final String mobile,final String aadhar,final String pan,final String dob,final String imageid,final String address,final String supervisor){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = fname ;

                String EmailHolder = lname ;
                String EmailHolder2 = mobile ;
                String EmailHolder3 = aadhar ;
                String EmailHolder4 = pan ;
                String EmailHolder5 = dob ;
                String EmailHolder6 = imageid ;
                String EmailHolder7 = address;
                String EmailHolder8 = supervisor;


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("fname", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("lname", EmailHolder));
                nameValuePairs.add(new BasicNameValuePair("mobile", EmailHolder2));
                nameValuePairs.add(new BasicNameValuePair("address", EmailHolder7));
                nameValuePairs.add(new BasicNameValuePair("aadhar", EmailHolder3));
                nameValuePairs.add(new BasicNameValuePair("pan", EmailHolder4));
                nameValuePairs.add(new BasicNameValuePair("dob", EmailHolder5));
                nameValuePairs.add(new BasicNameValuePair("imageid", EmailHolder6));
                nameValuePairs.add(new BasicNameValuePair("supervisor", EmailHolder8));

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
                String str = imageid;
                Toast.makeText(MainActivity2.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), FingerPrintStoreActivity.class);
                intent.putExtra("message_key",Temp7);
                startActivity(intent);
                loadid();
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                finish();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(fname, lname, mobile, address, aadhar, pan, dob,imageid,supervisor);
    }

}