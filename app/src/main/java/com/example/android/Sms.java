package com.example.android;

import android.os.StrictMode;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import androidx.appcompat.app.AppCompatActivity;

public class Sms extends AppCompatActivity
{

    private Button sendBtn;
    private EditText msfEt;
    private EditText numEt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        sendBtn = (Button)findViewById(R.id.snd_btn);
        numEt = (EditText)findViewById(R.id.et_num);
        msfEt = (EditText)findViewById(R.id.et_msg);



        sendBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(!(numEt.getText().toString() == null && msfEt.getText().toString() == null))
                {
                    String url ="http://192.168.1.106:80/android/send_sms.php?phone="+numEt.getText().toString()+"&msg="+msfEt.getText().toString();
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder().url(url).build();

                    try
                    {
                        Response response=client.newCall(request).execute();
                        String data=response.body().string();

                        if (!response.isSuccessful())
                        {
                            Toast.makeText(Sms.this, "Not Send", Toast.LENGTH_SHORT).show();
                            throw new IOException("Unexpected code " + response);
                        }
                        Toast.makeText(Sms.this, "Send Massage Successfully", Toast.LENGTH_SHORT).show();

                        return ;
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Toast.makeText(Sms.this, "please enter number and massage", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
