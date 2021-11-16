package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.VolleyError;
import com.example.android.http.AsyncResponse;
import com.example.android.http.HttpCall;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LeaveStatus extends AppCompatActivity {
    private final String filename2 = "address.txt";
    String file;
    private long pressedTime;
    TextView tex;
    EditText etx;
    int PERMISSION_ID = 44;
    Button Logi;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    LeaveConstructor leaveconstructor;
    TextView text_card,text_card_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_status);
        readData();
        tex = (TextView) findViewById(R.id.textView63);

        //FINGERSTRING.setVisibility(View.GONE);
        etx = findViewById(R.id.idBox71);
        //idBox.setText(e_id);
        CardView cardView = (CardView) findViewById(R.id.card_view999);
        cardView.setRadius(20F);
        text_card = findViewById(R.id.text_card431);
        text_card_contact = findViewById(R.id.text_card23);
        Logi = (Button) findViewById(R.id.fi);


        Logi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> reqMap = new HashMap<>();
                reqMap.put("empId", etx.getText().toString());
                HttpCall.makeFormRequest(LeaveStatus.this, file+"/android/leave_status.php", reqMap, new AsyncResponse() {
                    @Override
                    public void postExecute(String response) {
                        leaveconstructor = new Gson().fromJson(response, LeaveConstructor.class);
//                        attendance.updateArray();
                        // loadfingers();
                        etx.setClickable(false);
                        etx.setFocusable(false);
                        String[] names = new String[]{"Name.:-" +(leaveconstructor.getname()),"Status:-" +(leaveconstructor.getstatus()),"Leave Date:-" +(leaveconstructor.getstart()),"To -" +(leaveconstructor.getend()),"No. of Days:" +(leaveconstructor.getdays())};
                        //text_card.setText(biometric.getFirstname());
//                        names = new String[] {"City.:-" + city, "State.:-" + state, "Address:-" + add, "Zip:-" + zip};
                        text_card.setText(Arrays.toString(names));
                        //text_card_contact.setText(biometric.getContact_info());
                        cardView.setVisibility(View.VISIBLE);
                        Logi.setVisibility(View.GONE);
                        Log.i("TAG", "postExecute: ");
                    }

                    @Override
                    public void postError(VolleyError error) {
                        Toast.makeText(getBaseContext(), "error1", Toast.LENGTH_SHORT).show();


                    }
                });
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
        printMessage("reading to file " + filename2 + " completed..");
    }





    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            Intent intent = new Intent(LeaveStatus.this, MainActivity1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(LeaveStatus.this, Custom_Action.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }





}