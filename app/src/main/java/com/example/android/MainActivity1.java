package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;

import java.util.ArrayList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity1 extends AppCompatActivity {

    ImageView imgClick;
    private ListView lv;

    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;
    FloatingActionButton mAddFab, mAddAlarmFab, mAddPersonFab;
    TextView addAlarmActionText, addPersonActionText;
    Boolean isAllFabsVisible;

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


        mAddPersonFab.setOnClickListener(
                new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(MainActivity1.this, "Person Added", Toast.LENGTH_SHORT).show();
        }
    });

        mAddAlarmFab.setOnClickListener(
                new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(MainActivity1.this, "Alarm Added", Toast.LENGTH_SHORT).show();
        }
    });


                        // created new array list..
        recyclerDataArrayList = new ArrayList<>();

        // added data to array list
        recyclerDataArrayList.add(new RecyclerData("Enroll Employee", R.drawable.employee));
        recyclerDataArrayList.add(new RecyclerData("Enroll Supervisor ", R.drawable.hierarchy));
        recyclerDataArrayList.add(new RecyclerData("Precise Location Share", R.drawable.clipboard));
        recyclerDataArrayList.add(new RecyclerData("Employee List", R.drawable.clipboard));
        recyclerDataArrayList.add(new RecyclerData("Attendance List", R.drawable.search));
        recyclerDataArrayList.add(new RecyclerData("Search Supervisor", R.drawable.search));
        recyclerDataArrayList.add(new RecyclerData("Mark Attendance", R.drawable.immigration));
        recyclerDataArrayList.add(new RecyclerData("Upload Data", R.drawable.cloud));
        recyclerDataArrayList.add(new RecyclerData("Submit a Request", R.drawable.request));

        // added data from arraylist to adapter class.
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(recyclerDataArrayList, this);

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}