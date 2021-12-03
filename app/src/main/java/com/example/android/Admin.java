package com.example.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.DimType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class Admin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BoomMenuButton.OnSubButtonClickListener {

    ImageView imgClick;
    private ListView lv;
    private long pressedTime;
    private final String filename2 = "address.txt";
    String filess;


    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;
    FloatingActionButton mAddFab, mAddAlarmFab, mAddPersonFab;
    TextView addAlarmActionText, addPersonActionText;
    Boolean isAllFabsVisible;
    WebView simpleWebView;
    ProgressBar progressBar;
    private Fragment fragment;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    // private CoordinatorLayout coordLay;
    private ConstraintLayout coordLay;
    private FragmentManager fm;
    private Context mContext;
    private View mCustomView;
    private BoomMenuButton boomMenuButton;
    private BoomMenuButton boomMenuButtonInActionBar;
    private BoomMenuButton boomInfo;
    private boolean isInit = false;
    TextView ip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        recyclerView = findViewById(R.id.idCourseRV);
        mAddFab = findViewById(R.id.add_fab);
        mAddAlarmFab = findViewById(R.id.add_alarm_fab);
        mAddPersonFab = findViewById(R.id.add_person_fab);

        addAlarmActionText = findViewById(R.id.add_alarm_action_text);
        addPersonActionText = findViewById(R.id.add_person_action_text);

        ActionBar toolbar = getSupportActionBar();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        coordLay = (ConstraintLayout) findViewById(R.id.coordLayy);
        //coordLay = (CoordinatorLayout) findViewById(R.id.coordLayy);
        readData();
        CardView cardView = (CardView) findViewById(R.id.card_view990);
        cardView.setVisibility(View.INVISIBLE);

        //supervisorid.setText(str);
        // ip.setText(file);

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
        ip = findViewById(R.id.textView211);




        mContext = this;

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






        mAddAlarmFab.setVisibility(View.GONE);
        mAddPersonFab.setVisibility(View.GONE);
        addAlarmActionText.setVisibility(View.GONE);
        addPersonActionText.setVisibility(View.GONE);

        isAllFabsVisible = false;

        mAddFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {
                            mAddAlarmFab.show();
                            mAddPersonFab.show();
                            addAlarmActionText.setVisibility(View.VISIBLE);
                            addPersonActionText.setVisibility(View.VISIBLE);
                            isAllFabsVisible = true;
                        } else {
                            mAddAlarmFab.hide();
                            mAddPersonFab.hide();
                            addAlarmActionText.setVisibility(View.GONE);
                            addPersonActionText.setVisibility(View.GONE);

                            isAllFabsVisible = false;
                        }
                    }
                });


        mAddAlarmFab.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        setContentView(R.layout.leave_form);
                        simpleWebView = (WebView) findViewById(R.id.webView);
                        progressBar = (ProgressBar) findViewById(R.id.progress);
                        String url = "https://forms.office.com/Pages/ResponsePage.aspx?id=DQSIkWdsW0yxEjajBLZtrQAAAAAAAAAAAANAAb1i_O9UNllVN1dUUkxWR1U1UFpUSldKRk5JRUw5VC4u";
                        simpleWebView.getSettings().setJavaScriptEnabled(true);
                        simpleWebView.getSettings().setLoadsImagesAutomatically(true);
                        simpleWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                        simpleWebView.loadUrl(url);
                        simpleWebView.setWebViewClient(new MyWebViewClient());

                        Toast.makeText(Admin.this, "Expense Form", Toast.LENGTH_SHORT).show();
                    }

                    class MyWebViewClient extends WebViewClient {
                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {
                            super.onPageStarted(view, url, favicon);
                        }

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

        mAddPersonFab.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        setContentView(R.layout.leave_form);
                        simpleWebView = (WebView) findViewById(R.id.webView);
                        progressBar = (ProgressBar) findViewById(R.id.progress);
                        String url = "https://forms.office.com/Pages/ResponsePage.aspx?id=DQSIkWdsW0yxEjajBLZtrQAAAAAAAAAAAANAAb1i_O9URTJYQjJNVU0xV0E5MEpXNjQzV0hPR1RQTS4u";
                        simpleWebView.getSettings().setJavaScriptEnabled(true);
                        simpleWebView.getSettings().setLoadsImagesAutomatically(true);
                        simpleWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                        simpleWebView.loadUrl(url);
                        simpleWebView.setWebViewClient(new MyWebViewClient());

                        Toast.makeText(Admin.this, "Leave Form", Toast.LENGTH_SHORT).show();

                    }

                    class MyWebViewClient extends WebViewClient {
                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {
                            super.onPageStarted(view, url, favicon);
                        }

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


        // created new array list..
        recyclerDataArrayList = new ArrayList<>();

        // added data to array list
        recyclerDataArrayList.add(new RecyclerData("Dash Boad", R.drawable.employee));
        recyclerDataArrayList.add(new RecyclerData("Web Layout ", R.drawable.hierarchy));
        recyclerDataArrayList.add(new RecyclerData("Change Signature", R.drawable.employee));
 //       recyclerDataArrayList.add(new RecyclerData("Control Panel", R.drawable.clipboard));
//        recyclerDataArrayList.add(new RecyclerData("Attendance Check", R.drawable.search));
//        recyclerDataArrayList.add(new RecyclerData("Project Details", R.drawable.search));
//        recyclerDataArrayList.add(new RecyclerData("Mark Attendance", R.drawable.ic_baseline_fingerprint_24));
//        recyclerDataArrayList.add(new RecyclerData("Upload Data", R.drawable.cloud));
        //      recyclerDataArrayList.add(new RecyclerData("Submit a Request", R.drawable.request));

        // added data from arraylist to adapter class.
        AdminAdapter adapter = new AdminAdapter(recyclerDataArrayList, this);

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }


    public void  clickEvent(View v)
    {
        Toast.makeText(getBaseContext(), "Press back ", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Toast.makeText(getBaseContext(), "Press back ", Toast.LENGTH_SHORT).show();
                //  drawer.isDrawerOpen(GravityCompat.START);

                //       Open Close Drawer Layout
                if(drawer.isDrawerOpen(GravityCompat.START)){
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.isDrawerOpen(GravityCompat.END);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_home:
                Intent intent = new Intent(Admin.this, MainActivity4.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();

                navigationView.setCheckedItem(id);
                break;
            case R.id.nav_notification:
                Intent intent2 = new Intent(Admin.this, mark_attendance.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent2);
                finish();
                navigationView.setCheckedItem(id);
                break;
            case R.id.nav_settings:
                Intent intent3 = new Intent(Admin.this, MainActivity51.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent3);
                finish();
                navigationView.setCheckedItem(id);
                break;
            case R.id.nav_leave:
                setContentView(R.layout.leave_form);
                simpleWebView = (WebView) findViewById(R.id.webView);
                progressBar = (ProgressBar) findViewById(R.id.progress);
                String url = "https://forms.office.com/Pages/ResponsePage.aspx?id=DQSIkWdsW0yxEjajBLZtrQAAAAAAAAAAAANAAb1i_O9URTJYQjJNVU0xV0E5MEpXNjQzV0hPR1RQTS4u";
                simpleWebView.getSettings().setJavaScriptEnabled(true);
                simpleWebView.getSettings().setLoadsImagesAutomatically(true);
                simpleWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                simpleWebView.loadUrl(url);
                simpleWebView.setWebViewClient(new MyWebViewClient());

                Toast.makeText(Admin.this, "Leave Form", Toast.LENGTH_SHORT).show();

                navigationView.setCheckedItem(id);
                break;
            case R.id.nav_salary:
                Intent intent4 = new Intent(Admin.this, Scanner.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent4);
                finish();
                navigationView.setCheckedItem(id);
                break;

            case R.id.action_settings:
                Intent intent6 = new Intent(Admin.this, MainActivity6.class);
                intent6.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent6);
                finish();
                navigationView.setCheckedItem(id);
                break;

            case R.id.nav_scanner:
                Intent intent5 = new Intent(Admin.this, Employee_Login.class);
                intent5.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent5);
                finish();
                navigationView.setCheckedItem(id);
                break;
        }
        // close drawer after clicking the menu item
        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
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

    private void initInfoBoom() {

        Drawable[] drawables = new Drawable[3];
        int[] drawablesResource = new int[]{
                R.drawable.ic_baseline_gps_fixed_24,
                R.drawable.fingerprint1,
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
                            Intent intent = new Intent(Admin.this, gps_s.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            finish();
                            Toast.makeText(mContext, "PRECISE LOCATION", Toast.LENGTH_SHORT).show();
                        } else if (buttonIndex == 1) {
                            Intent intent = new Intent(Admin.this, Attendance_check.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            finish();
                            Toast.makeText(mContext, "User Attendance Portal", Toast.LENGTH_SHORT).show();

                        } else if (buttonIndex == 2) {
                            setContentView(R.layout.leave_form);
                            simpleWebView = (WebView) findViewById(R.id.webView);
                            progressBar = (ProgressBar) findViewById(R.id.progress);
                            String url = "http://192.168.1.106:80/Android/Payroll_and_Attendance_system/admin/crud/index.php";
                            simpleWebView.getSettings().setJavaScriptEnabled(true);
                            simpleWebView.getSettings().setLoadsImagesAutomatically(true);
                            simpleWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                            simpleWebView.loadUrl(url);
                            simpleWebView.setWebViewClient(new MyWebViewClient());

                            Toast.makeText(Admin.this, "View Employee List", Toast.LENGTH_SHORT).show();
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
            filess = temp.toString();
//            ip.setText(temp.toString());
            fin.close();
            // UserLoginFunction(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        printMessage("reading to file " + filess + " completed..");
        //ip.setText(filess.toString());
    }






    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            if (boomMenuButton.isClosed()
                    && boomMenuButtonInActionBar.isClosed()
                    && boomInfo.isClosed()) {
                super.onBackPressed();
            } else {
                boomMenuButton.dismiss();
                boomMenuButtonInActionBar.dismiss();
                boomInfo.dismiss();
                Intent intent = new Intent(Admin.this, MainActivity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            }

        }
        pressedTime = System.currentTimeMillis();
    }


}


