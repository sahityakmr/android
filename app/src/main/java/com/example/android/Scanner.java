package com.example.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.zxing.client.android.Intents;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Scanner extends AppCompatActivity implements View.OnClickListener {

    TextView txtScanResult;
    Button btnScan;
    JSONObject jObj = null;
    String json = "";
    String scanResult,file;
    String serverResponse=null;
    ProgressBar loading;
    public ProgressDialog pDialog;

    //create JSON parser Object
    JSONParser jParser = new JSONParser();
    private final String filename2 = "address.txt";
    //server url
    private static String getEmployeeInformationUrl;
    //declare employee JSON variables
    private final String TAG_ID = "id";
    private final String TAG_LASTNAME = "lastname";
    private final String TAG_FIRSTNAME = "firstname";
   // private final String TAG_contact_info = "contact_info";

    //declare JSONArray
    JSONArray employeeInformation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);
        txtScanResult=(TextView)findViewById(R.id.txtScanResult);
        btnScan=(Button)findViewById(R.id.btnScan);
        readData();
        btnScan.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }
    public void scan(int code) {
//        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//        intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
//        startActivityForResult(intent, 0);


        Intent intentScan = new Intent(Settings.ZXING_SCAN_INTENT);
        intentScan.addCategory(Intent.CATEGORY_DEFAULT);

        startActivityForResult(intentScan, code);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == Settings.ZXING_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                JSONObject obj = new JSONObject();
                try {

                    obj.put(Settings.ZXING_TEXT, intent.getStringExtra("SCAN_RESULT"));
                    obj.put(Settings.ZXING_FORMAT, intent.getStringExtra("SCAN_RESULT_FORMAT"));
                    obj.put(Settings.ZXING_CANCELLED, false);
                    Log.e("SCAN RESULT", intent.getStringExtra("SCAN_RESULT"));
                    scanResult=intent.getStringExtra("SCAN_RESULT");
                    new LoadEmployeeInformation().execute();

                } catch (JSONException e) {
                    // Log.d(LOG_TAG, "This should never happen");
                }
                //this.success(new PluginResult(PluginResult.Status.OK, obj), this.callback);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put(Settings.ZXING_TEXT, "");
                    obj.put(Settings.ZXING_FORMAT, "");
                    obj.put(Settings.ZXING_CANCELLED, true);
                } catch (JSONException e) {
                    // Log.d(LOG_TAG, "This should never happen");
                }
                //this.success(new PluginResult(PluginResult.Status.OK, obj), this.callback);
            } else {
                //this.error(new PluginResult(PluginResult.Status.ERROR), this.callback);
            }
        }
    }

    @Override
    public void onClick(View view) {
        scan(Settings.ZXING_REQUEST_CODE);
    }

    /*
	 * Load employee information from server
	 */
    class LoadEmployeeInformation extends AsyncTask<String, String, String> {
        String success;
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(Scanner.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            getEmployeeInformationUrl=file+"/Android/test.php";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", scanResult));
            JSONObject json = jParser.makeHttpRequest(getEmployeeInformationUrl, "GET", params);

            if(json!=null){
                try {
                    // Json from server is not null
                    // verify if information are got
                    if(json.getString("success").equals("1")){
                        JSONArray result=json.getJSONArray("information");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject c = result.getJSONObject(i);

                            // get information
                            String id = c.getString(TAG_ID);
                            String lastname = c.getString(TAG_LASTNAME);
                            String firstname = c.getString(TAG_FIRSTNAME);
                            //String contact_info = c.getString(TAG_FIRSTNAME);
                            serverResponse=id+"\n"+lastname+"\n"+firstname;
                        }
                    }
                } catch (JSONException e) {
                    Log.e("error: ", e.getMessage());
                }
            }
            return null;
        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if(serverResponse!=null){
                        txtScanResult.setText(serverResponse);
                        btnScan.setVisibility(View.INVISIBLE);
                    }else{
                        txtScanResult.setText("this employee ID does not exist!!!");
                    }
                }

            });

        }

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
    }

}
