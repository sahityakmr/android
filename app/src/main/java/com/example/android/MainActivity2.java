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
import android.widget.Toast;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity2 extends AppCompatActivity  {
    //implements View.OnClickListener
    String ServerURL = "http://192.168.29.218:80/Android/get_data.php" ;
    EditText fname, lname,mobile,aadhar,pan,dob, imageid ;
    Button button;
    String TempName, Temp2,Temp3,Temp4,Temp5,Temp6,Temp7;
    DatePickerDialog picker;
    //    Button btnGet;
//    TextView tvw;
//    Boolean CheckEditText ;
//    public Calendar mcalendar;
//
//    public int day,month,year;
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
        // dob.setInputType(InputType.TYPE_NULL);

        //   dob.setOnClickListener(this);
//        day=mcalendar.get(Calendar.DAY_OF_MONTH);
//        year=mcalendar.get(Calendar.YEAR);
//        month=mcalendar.get(Calendar.MONTH);
        button = (Button) findViewById(R.id.button);
        //   btnGet = (Button)findViewById(R.id.button99);
        // button.setOnClickListener(this);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                GetData();

                InsertData(TempName, Temp2, Temp3, Temp4, Temp5, Temp6, Temp7);

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

    }

    public void InsertData(final String fname, final String lname,final String mobile,final String aadhar,final String pan,final String dob,final String imageid){

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


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("fname", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("lname", EmailHolder));
                nameValuePairs.add(new BasicNameValuePair("mobile", EmailHolder2));
                nameValuePairs.add(new BasicNameValuePair("aadhar", EmailHolder3));
                nameValuePairs.add(new BasicNameValuePair("pan", EmailHolder4));
                nameValuePairs.add(new BasicNameValuePair("dob", EmailHolder5));
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
                String str = imageid;
                Toast.makeText(MainActivity2.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), FingerPrintStoreActivity.class);
                intent.putExtra("message_key",Temp7);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                finish();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(fname, lname, mobile, aadhar, pan, dob,imageid);
    }

}
