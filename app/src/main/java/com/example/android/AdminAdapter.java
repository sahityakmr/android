package com.example.android;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileInputStream;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import java.io.IOException;
import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.RecyclerViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private final ArrayList<RecyclerData> courseDataArrayList;
    private final Context mcontext;
    WebView simpleWebView;
    ProgressBar progressBar;
    private final String filename2 = "address.txt";
    String filess;

    public AdminAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Context mcontext) {
        this.courseDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder,  int position) {
        // Set the data to textview and imageview.
        RecyclerData recyclerData = courseDataArrayList.get(position);
        holder.courseTV.setText(recyclerData.getTitle());
        holder.courseIV.setImageResource(recyclerData.getImgid());
        //holder.currentItem.getIntent().getStringExtra();
        holder.cardView.setTag(position);
        // String s=holder.cardView.findViewById();
        //holder.currentItem= ClipData.Item.();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (position == 0) {
                    readData();
                    Intent intent = new Intent(v.getContext(), DashBoard.class);
                    intent.putExtra("filess",filess);
                    mcontext.startActivity(intent);

                    Toast.makeText(v.getContext(), "Welcome To Admin Session", Toast.LENGTH_LONG).show();
                }

                if (position == 1) {
                    Intent intent = new Intent(v.getContext(), UserLoginActivity1.class);
                    mcontext.startActivity(intent);

                    Toast.makeText(v.getContext(), "Login to Enroll", Toast.LENGTH_LONG).show();
                }



                if (position == 2) {
                    Intent intent = new Intent(v.getContext(), Employee_Login.class);
                    mcontext.startActivity(intent);

                    Toast.makeText(v.getContext(), "Please Register your device.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }




    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return courseDataArrayList.size();
    }




    private void readData() {
        try {
            FileInputStream fin = mcontext.openFileInput(filename2);
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
    }




    // public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView courseTV;
        private int position;
        private final ImageView courseIV;
        public CardView cardView;
        public ClipData.Item currentItem;
        public View view;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            courseTV = itemView.findViewById(R.id.idTVCourse);
            courseIV = itemView.findViewById(R.id.idIVcourseIV);
            cardView = itemView.findViewById(R.id.cv_id);




        }





    }
}