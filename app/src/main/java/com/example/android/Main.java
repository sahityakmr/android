package com.example.android;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Main extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<FruitModel> imageModelArrayList;
    private FruitAdapter adapter;

    private final int[] myImageList = new int[]{R.drawable.ic_baseline_fingerprint_24, R.drawable.ic_baseline_fingerprint_24,R.drawable.ic_baseline_fingerprint_24, R.drawable.ic_baseline_fingerprint_24,R.drawable.ic_baseline_fingerprint_24,R.drawable.ic_baseline_fingerprint_24,R.drawable.ic_baseline_fingerprint_24,R.drawable.ic_baseline_fingerprint_24,R.drawable.ic_baseline_fingerprint_24,R.drawable.ic_baseline_fingerprint_24};
    private final String[] myImageNameList = new String[]{"Fingure1","Fingure2" ,"Fingure3","Fingure4","Fingure5","Fingure6","Fingure7","Fingure8","Fingure9","Fingure10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horizontal);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        imageModelArrayList = eatFruits();
        adapter = new FruitAdapter(this, imageModelArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

    }

    private ArrayList<FruitModel> eatFruits(){

        ArrayList<FruitModel> list = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            FruitModel fruitModel = new FruitModel();
            fruitModel.setName(myImageNameList[i]);
            fruitModel.setImage_drawable(myImageList[i]);
            list.add(fruitModel);
        }

        return list;
    }

}

