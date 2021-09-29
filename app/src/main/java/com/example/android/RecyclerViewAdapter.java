package com.example.android;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;

import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<RecyclerData> courseDataArrayList;
    private Context mcontext;

    public RecyclerViewAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Context mcontext) {
        this.courseDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        // int viewType
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
//        view.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View v) {
//                if(position == 0)
//
//                {
//                    Log.i("W4K","Click-"+position);
//
//                    //Log.d(TAG, "onClick: card view tapped");
//                    Intent intent = new Intent(v.getContext(), MainActivity51.class);
//                    mcontext.startActivity(intent);
//                    Toast.makeText(v.getContext(), "Please fill all form fields.", Toast.LENGTH_LONG).show();
//
//
//
//
//
//                }
//
//                if(position==1)
//
//                {
//                    //Log.d(TAG, "onClick: card view tapped");
//                    Intent intent = new Intent(v.getContext(), MainActivity3.class);
//                    mcontext.startActivity(intent);
//                    Toast.makeText(v.getContext(), "Please fill all form fields.", Toast.LENGTH_LONG).show();
//
//                }
//
//            }
//        });
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
                    Intent intent = new Intent(v.getContext(), UserLoginActivity.class);
                    mcontext.startActivity(intent);

                    Toast.makeText(v.getContext(), "Login to Enroll", Toast.LENGTH_LONG).show();
                }

                if (position == 1) {
                    Intent intent = new Intent(v.getContext(), UserLoginActivity1.class);
                    mcontext.startActivity(intent);

                    Toast.makeText(v.getContext(), "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }

                if (position == 3) {
                    Intent intent = new Intent(v.getContext(), MainActivity4.class);
                    mcontext.startActivity(intent);

                    Toast.makeText(v.getContext(), "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }

                if (position == 8) {
                    Intent intent = new Intent(v.getContext(), MainActivity51.class);
                    mcontext.startActivity(intent);

                    Toast.makeText(v.getContext(), "Please Register your device.", Toast.LENGTH_LONG).show();
                }

                if (position == 7) {
                    Intent intent = new Intent(v.getContext(), MainActivity5.class);
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

    // View Holder Class to handle Recycler View.
//    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
//
//        private TextView courseTV;
//        private ImageView courseIV;
//        private CardView cardView;
//        public ClipData.Item currentItem;
//        public View view;
//
//        public RecyclerViewHolder(@NonNull View itemView) {
//            super(itemView);
//            courseTV = itemView.findViewById(R.id.idTVCourse);
//            courseIV = itemView.findViewById(R.id.idIVcourseIV);
//            cardView = itemView.findViewById(R.id.cv_id);
//
//
//
//
//        }
//    }


   // public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
   public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView courseTV;
        private int position;
        private ImageView courseIV;
        public CardView cardView;
        public ClipData.Item currentItem;
        public View view;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            courseTV = itemView.findViewById(R.id.idTVCourse);
            courseIV = itemView.findViewById(R.id.idIVcourseIV);
            cardView = itemView.findViewById(R.id.cv_id);
//            itemView.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//            //your implementation.
//            if (position==0) {
//                Intent intent = new Intent(v.getContext(), MainActivity51.class);
//                mcontext.startActivity(intent);
//
//                Toast.makeText(v.getContext(), "Please fill all form fields.", Toast.LENGTH_LONG).show();
//            }
//
//            if (position==1) {
//                Intent intent = new Intent(v.getContext(), MainActivity3.class);
//                mcontext.startActivity(intent);
//
//                Toast.makeText(v.getContext(), "Please fill all form fields.", Toast.LENGTH_LONG).show();
//            }


          //  https://www.journaldev.com/12372/android-recyclerview-example
          //  https://stackoverflow.com/questions/44238018/recyclerview-onclick-for-multiple-buttons-and-handling-from-activity
          //  https://github.com/hasancse91/Android-CardView-RecyclerView/commit/18a307edbd856f3ccf939aee248ee4542c632b7b


        }





    }
   // public String getIdRecyclerData (String link){return link.substring(link.lastIndexOf("=")+1);}
}