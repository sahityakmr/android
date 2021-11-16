package com.example.android;

import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.MyViewHolder>  {

   // private LayoutInflater inflater;
    private final ArrayList<FruitModel> imageModelArrayList;
    private final ArrayList<FruitModel> imageModelArrayList1;
    private final Context ctx;


    public FruitAdapter(Context ctx, ArrayList<FruitModel> imageModelArrayList){

      //  inflater = LayoutInflater.from(ctx);
        this.imageModelArrayList = imageModelArrayList;
        this.imageModelArrayList1 = imageModelArrayList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

     //   View view = LayoutInflater.inflate(R.layout.recycler_item, parent, false);
          View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.iv.setImageResource(imageModelArrayList.get(position).getImage_drawable());
        holder.time.setText(imageModelArrayList.get(position).getName());
        holder.vi.setImageResource(imageModelArrayList1.get(position).getImage_drawable());
        holder.cardView.setTag(position);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (position == 0) {
                    ((FingerPrintStoreActivity)ctx).fin1();
//                    holder.iv.setVisibility(View.GONE);
//                    holder.vi.setVisibility(View.VISIBLE);

                    Toast.makeText(v.getContext(), "Finger1", Toast.LENGTH_LONG).show();
                }

                if (position == 1) {
                    ((FingerPrintStoreActivity)ctx).fin2();
                    Toast.makeText(v.getContext(), "Finger2", Toast.LENGTH_LONG).show();
                  }


                if (position == 2) {
                    ((FingerPrintStoreActivity)ctx).fin3();
                    Toast.makeText(v.getContext(), "Finger3", Toast.LENGTH_LONG).show();
                }

                if (position == 3) {
                    ((FingerPrintStoreActivity)ctx).fin4();
                    Toast.makeText(v.getContext(), "Finger4", Toast.LENGTH_LONG).show();
                }

                if (position == 4) {
                    ((FingerPrintStoreActivity)ctx).fin5();
                    Toast.makeText(v.getContext(), "Finger5", Toast.LENGTH_LONG).show();
                }

                if (position == 5) {
                    ((FingerPrintStoreActivity)ctx).fin6();
                    Toast.makeText(v.getContext(), "Finger6", Toast.LENGTH_LONG).show();
                }

                if (position == 6) {
                    ((FingerPrintStoreActivity)ctx).fin7();
                    Toast.makeText(v.getContext(), "Finger7", Toast.LENGTH_LONG).show();
                }

                if (position == 7) {
                    ((FingerPrintStoreActivity)ctx).fin8();
                    Toast.makeText(v.getContext(), "Finger8", Toast.LENGTH_LONG).show();
                }

                if (position == 8) {
                    ((FingerPrintStoreActivity)ctx).fin9();
                    Toast.makeText(v.getContext(), "Finger9", Toast.LENGTH_LONG).show();
                }

                if (position == 9) {
                    ((FingerPrintStoreActivity)ctx).fin10();
                    Toast.makeText(v.getContext(), "Finger10", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView time;
        ImageView iv;
        ImageView vi;
        public CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            time = (TextView) itemView.findViewById(R.id.tv);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            vi = (ImageView) itemView.findViewById(R.id.vi);
            cardView = itemView.findViewById(R.id.card_view6);
        }

    }

}
