package com.example.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.VolleyError;
import com.example.android.http.AsyncResponse;
import com.example.android.http.HttpCall;
import com.google.android.material.navigation.NavigationView;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.DimType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Custom_Action extends AppCompatActivity
        implements
        BoomMenuButton.OnSubButtonClickListener,
        NavigationView.OnNavigationItemSelectedListener
         {

    private BoomMenuButton boomMenuButton;
    private BoomMenuButton boomMenuButtonInActionBar;
    private BoomMenuButton boomInfo;

    private Context mContext;
    private View mCustomView;
    private final String filename2 = "address.txt";
    private final String filename3 = "employeeid.txt";
    String file3,file;
    private RadioGroup placeTypeGroup;
    private RadioButton[] placeTypeButtons;
    private final int[] CirclePlaceTypes = new int[]{1, 2, 4, 2, 4, 6, 4, 3, 2};
    private final int[] HamPlaceTypes = new int[]{1, 1, 1, 1};

    private boolean isInit = false;

     private DrawerLayout drawer;
     private NavigationView navigationView;
     private ConstraintLayout coordLay;
     WebView simpleWebView;
     ProgressBar progressBar;

     Button getbtn, share;
     ListView listView;
     String[] names;
     public static final int RClP = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainc);
        readData();
        readData1();

        mContext = this;
        listView = findViewById(R.id.listview1);

        getbtn = findViewById(R.id.getlocationbtn1);
        //share = findViewById(R.id.b);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        LayoutInflater mInflater = LayoutInflater.from(this);

        mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText(R.string.app_name);

        boomMenuButtonInActionBar = (BoomMenuButton) mCustomView.findViewById(R.id.boom);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        ((Toolbar) mCustomView.getParent()).setContentInsetsAbsolute(0,0);

        boomMenuButton = (BoomMenuButton)findViewById(R.id.boom);

        boomInfo = (BoomMenuButton)mCustomView.findViewById(R.id.info);

      //  initViews();

        ActionBar toolbar = getSupportActionBar();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        coordLay = (ConstraintLayout) findViewById(R.id.coordLayy);
        //coordLay = (CoordinatorLayout) findViewById(R.id.coordLayy);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.app_name, R.string.app_name);
        //drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setHomeButtonEnabled(true);
        navigationView.setCheckedItem(R.id.nav_home);


        getbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayData(file3);
            }
        });


    }


    public void displayData(final String employeeid) {
                 Map<String, String> requestBodyMap = new HashMap<>();
                 requestBodyMap.put("employeeid", employeeid);
                 HttpCall.makeFormRequest(this, file+"/Android/employee_detail.php", requestBodyMap, new AsyncResponse() {
                     @Override
                     public void postExecute(String response) {
                         try {
                             JSONObject jsonObject = new JSONObject(response);
                             JSONObject bodyObject = jsonObject.getJSONObject("body");
                             String firstname = bodyObject.getString("firstname");
                             String lastname = bodyObject.getString("lastname");
                             String address = bodyObject.getString("address");
                             String contact_info = bodyObject.getString("contact_info");
                             String position_id = bodyObject.getString("position_id");
                             String schedule_id = bodyObject.getString("schedule_id");
                             String user_email = bodyObject.getString("user_email");
                             String password = bodyObject.getString("password");
//                             e.setText("Name : "+id);
//                             d.setText("Contact : "+guid);
                             //qr(id,guid,employeeid);
                             setUpdata(firstname,lastname,employeeid,address,contact_info,position_id,schedule_id,user_email,password);

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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (!isInit) {
            //initBoom();
            initInfoBoom();
        }
        isInit = true;
    }


    public void setUpdata(final String ids,final String guiddd,final String employeeids,final String guidd1,final String guidd2,final String guidd3,final String guidd4,final String guidd5,final String guidd6) {
         names = new String[] {"Name:-" + ids +" " +guiddd, "ID.:-" + employeeids, "Address:-" + guidd1, "Contact:-" + guidd2, "Designation:-" + guidd3, "Timing:-" + guidd4, "Supervisor:-" + guidd5, "Password:-" + guidd6};
         ArrayAdapter<String> adapter = new ArrayAdapter<String>(Custom_Action.this, android.R.layout.simple_list_item_1, names);
         listView.setAdapter(adapter);

     }



             private void initInfoBoom() {

        Drawable[] drawables = new Drawable[3];
        int[] drawablesResource = new int[]{
                R.drawable.ic_baseline_gps_fixed_24,
                R.drawable.finger,
                R.drawable.employee
        };
        for (int i = 0; i < 3; i++)
            drawables[i] = ContextCompat.getDrawable(mContext, drawablesResource[i]);

        int[][] colors = new int[3][2];
        for (int i = 0; i < 3; i++) {
            colors[i][1] = ContextCompat.getColor(mContext, R.color.black);
            colors[i][0] = Util.getInstance().getPressedColor(colors[i][1]);
        }

        // Now with Builder, you can init BMB more convenient
        new BoomMenuButton.Builder()
                .subButtons(drawables, colors, new String[]{"Location Share", "Attendance Check", "View Employees"})
                .button(ButtonType.HAM)
                .boom(BoomType.PARABOLA_2)
                .place(PlaceType.HAM_3_1)
                .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .subButtonTextColor(ContextCompat.getColor(mContext, R.color.black))
                .onSubButtonClick(new BoomMenuButton.OnSubButtonClickListener() {
                    @Override
                    public void onClick(int buttonIndex) {
                        if (buttonIndex == 0) {
                            Intent intent = new Intent(Custom_Action.this, gps_s.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            finish();
                            Toast.makeText(mContext, "PRECISE LOCATION", Toast.LENGTH_SHORT).show();
                        } else if (buttonIndex == 1) {
                            Intent intent = new Intent(Custom_Action.this, Attendance_check.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            finish();
                            Toast.makeText(mContext, "User Attendance Portal", Toast.LENGTH_SHORT).show();
                        } else if (buttonIndex == 2) {
                            setContentView(R.layout.leave_form);
                            simpleWebView = (WebView) findViewById(R.id.webView);
                            progressBar = (ProgressBar) findViewById(R.id.progress);
                            String url = file+"/Android/Payroll_and_Attendance_system/admin/crud/index.php";
                            simpleWebView.getSettings().setJavaScriptEnabled(true);
                            simpleWebView.getSettings().setLoadsImagesAutomatically(true);
                            simpleWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                            simpleWebView.loadUrl(url);
                            simpleWebView.setWebViewClient(new MyWebViewClient());

                            Toast.makeText(Custom_Action.this, "View Employee List", Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .init(boomInfo);
    }

    private void initBoom() {

        // Now with Builder, you can init BMB more convenient
        new BoomMenuButton.Builder()
                .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .onSubButtonClick(this)
                .dim(DimType.DIM_0)
                .init(boomMenuButtonInActionBar);
    }


    @Override
    public void onClick(int buttonIndex) {
        Toast.makeText(this, "On click " +
                boomMenuButton.getTextViews()[buttonIndex].getText().toString() +
                " button", Toast.LENGTH_SHORT).show();
    }

     @Override
     public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
         // Handle navigation view item clicks here.
         int id = menuItem.getItemId();
         switch (id) {
             case R.id.nav_home:
                 Intent intent = new Intent(Custom_Action.this,MainActivity1.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                 startActivity(intent);
                 finish();

                 navigationView.setCheckedItem(id);
                 break;


             case R.id.nav_leave:
                 Intent intent21 = new Intent(Custom_Action.this, Leave.class);
                 intent21.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                 startActivity(intent21);
                 finish();

                 Toast.makeText(Custom_Action.this, "Leave Form", Toast.LENGTH_SHORT).show();

                 navigationView.setCheckedItem(id);
                 break;

             case R.id.nav_salary:
                 Intent intent5 = new Intent(Custom_Action.this, Salary.class);
                 intent5.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                 startActivity(intent5);
                 finish();
                 navigationView.setCheckedItem(id);
                 break;

             case R.id.nav_status:
                 Intent intent77 = new Intent(Custom_Action.this, LeaveStatus.class);
                 intent77.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                 startActivity(intent77);
                 finish();
                 navigationView.setCheckedItem(id);
                 break;

             case R.id.nav_idcard:
                 Intent intent79 = new Intent(Custom_Action.this, IdCard.class);
                 intent79.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                 startActivity(intent79);
                 finish();
                 navigationView.setCheckedItem(id);
                 break;

             case R.id.action_settings:
                 Intent intent6 = new Intent(Custom_Action.this, MainActivity6.class);
                 intent6.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                 startActivity(intent6);
                 finish();
                 navigationView.setCheckedItem(id);
                 break;
         }
         // close drawer after clicking the menu item
         // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         drawer.closeDrawer(GravityCompat.START);
         return false;
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
                 if (boomMenuButton.isClosed()
                         && boomMenuButtonInActionBar.isClosed()
                         && boomInfo.isClosed()) {
                     super.onBackPressed();
                 } else {
                     boomMenuButton.dismiss();
                     boomMenuButtonInActionBar.dismiss();
                     boomInfo.dismiss();
                 }
             }

             private void readData1() {
                 try {
                     FileInputStream fin = openFileInput(filename3);
                     int a;
                     StringBuilder temp = new StringBuilder();
                     while ((a = fin.read()) != -1) {
                         temp.append((char) a);
                     }

                     // setting text from the file.
                     file3 = temp.toString();
                     fin.close();
                     // UserLoginFunction(fileContent);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 printMessage("reading to file " + filename2 + " completed..");
             }






}
