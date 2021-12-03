package com.example.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import com.android.volley.VolleyError;
import com.example.android.http.AsyncResponse;
import com.example.android.http.HttpCall;
import com.bumptech.glide.Glide;

import org.apache.http.message.BasicHeaderIterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;

import de.hdodenhof.circleimageview.CircleImageView;

public class IdCard extends AppCompatActivity {
    TextView e,d,t,f;
    CircleImageView circle;
   private final String filename = "employeeid.txt";
    private final String filename2 = "address.txt";
    private long pressedTime;
    ImageView st;
    String file1,urli;
    List<String> listString = new ArrayList<String>();
    ProgressBar progressBarSubject;
    ListView SubjectListView;
    ArrayAdapter<String> arrayAdapter ;
    ImageView im;
    private ImageView qrCodeIV;

    String file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idcard);
        e = (TextView) findViewById(R.id.emailname);
        d = (TextView) findViewById(R.id.emailcontact);
        t = (TextView) findViewById(R.id.emailid);
        f = (TextView) findViewById(R.id.emailid211);
        st = findViewById(R.id.stamp);
        circle = (CircleImageView) findViewById(R.id.circle);
        im = (ImageView) findViewById(R.id.idIVQrcode);
        Bitmap bitmap;
        ButterKnife.bind(this);

        readData1();
        readData();

    }


    public void printMessage(String m) {
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    private void readData() {
        try {
            FileInputStream fin = openFileInput(filename);
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char) a);
            }

            // setting text from the file.
            file = temp.toString();
            t.setText("ID : "+temp.toString());
            fin.close();
            insertData(file);
            // UserLoginFunction(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
     //   printMessage("reading to file " + filename2 + " completed..");
    }


    private void readData1() {
        try {
            FileInputStream fin = openFileInput(filename2);
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char) a);
            }

            // setting text from the file.
            file1 = temp.toString();
            fin.close();
            // UserLoginFunction(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
     //   printMessage("reading to file " + filename2 + " completed..");
    }


    public void insertData(final String employeeid) {
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("employeeid", employeeid);
        HttpCall.makeFormRequest(this, file1+"/Android/idcard.php", requestBodyMap, new AsyncResponse() {
            @Override
            public void postExecute(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject bodyObject = jsonObject.getJSONObject("body");
                    String id = bodyObject.getString("id");
                    String guid = bodyObject.getString("guid");
                    String gui = bodyObject.getString("des");
                    e.setText("Name : "+id);
                    d.setText("Contact : "+guid);
                    f.setText("Designation : "+gui);
                    qr(id,guid,employeeid);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void postError(VolleyError error) {

            }
        });
    }
            public void qr(final String name,final String contact,final String idd) {
                StringBuilder textToSend = new StringBuilder();
                textToSend.append(name+" | "+contact+" | "+idd);
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(textToSend.toString(), BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    im.setImageBitmap(bitmap);
                    im.setVisibility(View.VISIBLE);

                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }



    public void circle1(final View view)
    {
        insert(file);
        //new IdCard.GetHttpResponse(IdCard.this).execute();
        // Do something in response to button click
    }


    public void insert(final String employeeid) {
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("employeeid", employeeid);
        HttpCall.makeFormRequest(this, file1+"/Android/q.php", requestBodyMap, new AsyncResponse() {
            @Override
            public void postExecute(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject bodyObject = jsonObject.getJSONObject("body");
                    urli = bodyObject.getString("url");
                    test(urli);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void postError(VolleyError error) {

            }
        });
    }

    private void test(final String a)
    {
        Glide.with(this)
                .load(urli)
                .into(circle);

    }

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Intent intent = new Intent(IdCard.this, Custom_Action.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

}
