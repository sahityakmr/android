package com.example.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.R;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
 
public class MainActivity4 extends FragmentActivity {

    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work
   // private static final String URL_PRODUCTS = "http://192.168.1.106:80/Android/Api.php";

    //a list to store all the products
    List<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;

    ListView SubjectListView;
    ProgressBar progressBarSubject;
    ProgressDialog progressDialog ;
   // String ServerURL = "http://192.168.1.106:80/Android/Subjects.php";
    EditText editText ;
    List<String> listString = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter ;
    private final String filename = "demoFile.txt";
    TextView fileContent;
    HashMap<String,String> hashMap = new HashMap<>();
    String finalResult,files;
    HttpParse httpParse = new HttpParse();
    private long pressedTime;
    private final String filename2 = "address.txt";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);





        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileContent = findViewById(R.id.content);

        //initializing the productlist
        productList = new ArrayList<>();

        //this method will fetch and parse json
        //to display it in recyclerview

        readData();
        read();
        loadProducts();





        SubjectListView = (ListView)findViewById(R.id.listview1);

        progressBarSubject = (ProgressBar)findViewById(R.id.progressBar);

        editText = (EditText)findViewById(R.id.edittext111);

        new MainActivity4.GetHttpResponse(MainActivity4.this).execute();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                MainActivity4.this.arrayAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
            fileContent.setText(temp.toString());
            fin.close();
            UserLoginFunction(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //printMessage("reading to file " + filename + " completed..");
    }



    public void UserLoginFunction(final TextView file){

        class UserLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(MainActivity4.this,"Loading Data","Please wait a Moment",true,false);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();

                if(httpResponseMsg.equalsIgnoreCase("Data Matched")){

                    finish();

                }
                else{

                    Toast.makeText(MainActivity4.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("file",params[0]);

                finalResult = httpParse.postRequest(hashMap, files+"/Android/Api.php");

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(file.getText().toString());
    }









    private void loadProducts() {
 

        StringRequest stringRequest = new StringRequest(Request.Method.GET, files+"/Android/Api.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("TAG", "onResponseWorking: " + response);
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                
                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
 
                                //adding the product to product list
                                productList.add(new Product(
                                        product.getString("firstname"),
                                        product.getString("contact_info"),
                                        product.getString("aadhar"),
                                        product.getString("image_name"),
                                        product.getString("lastname"),
                                        product.getString("photo")


                                ));
                            }
 
                            //creating adapter object and setting it to recyclerview
                            ProductAdapter adapter = new ProductAdapter(MainActivity4.this, productList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
 
                    }
                });
 
        //adding our stringrequest to queue 
        Volley.newRequestQueue(this).add(stringRequest);
    }


    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;

        String ResultHolder;
        ProgressDialog loading;

        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            loading = ProgressDialog.show(MainActivity4.this, "Loading...", null,true,false);
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            HttpServicesClass httpServiceObject = new HttpServicesClass(files+"/Android/Subjects.php");
            try
            {
                httpServiceObject.ExecutePostRequest();

                if(httpServiceObject.getResponseCode() == 200)
                {
                    ResultHolder = httpServiceObject.getResponse();

                    if(ResultHolder != null)
                    {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(ResultHolder);

                            JSONObject jsonObject;

                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                jsonObject = jsonArray.getJSONObject(i);

                                listString.add(jsonObject.getString("subjects")) ;

                            }
                        }
                        catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Toast.makeText(context, httpServiceObject.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            progressBarSubject.setVisibility(View.GONE);
            loading.dismiss();

         //   SubjectListView.setVisibility(View.VISIBLE);

            arrayAdapter = new ArrayAdapter<String>(MainActivity4.this,android.R.layout.simple_list_item_2, android.R.id.text1, listString);

          //  SubjectListView.setAdapter(arrayAdapter);

        }
    }

    private void read() {
        try {
            FileInputStream fin = openFileInput(filename2);
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char) a);
            }

            // setting text from the file.
            files = temp.toString();
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
            Intent intent = new Intent(MainActivity4.this, MainActivity1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}