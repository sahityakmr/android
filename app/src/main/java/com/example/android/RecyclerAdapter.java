package com.example.android;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<String> id = new ArrayList<>();
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> description = new ArrayList<>();
    private final Context mcontext;

    public RecyclerAdapter(Context mcontext, ArrayList<String> id, ArrayList<String> title, ArrayList<String> description) {

        this.mcontext = mcontext;
        this.id = id;
        this.title = title;
        this.description = description;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView idTextView;
        public TextView titleTextView;
        public TextView descriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            idTextView = itemView.findViewById(R.id.no);
            titleTextView = itemView.findViewById(R.id.title);
            descriptionTextView = itemView.findViewById(R.id.description);
        }
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

        RecyclerAdapter.ViewHolder viewHolder = new RecyclerAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.idTextView.setText(id.get(position));
        holder.titleTextView.setText(title.get(position));
        holder.descriptionTextView.setText(description.get(position));


        holder.descriptionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (position == 0) {
                    Intent intent = new Intent(v.getContext(), Credential.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    mcontext.startActivity(intent);

                    Toast.makeText(v.getContext(), "Login to Enroll", Toast.LENGTH_LONG).show();
                }

                if (position == 1) {
                    Intent intent = new Intent(v.getContext(), UserLoginActivity1.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    mcontext.startActivity(intent);

                    Toast.makeText(v.getContext(), "Login to Enroll", Toast.LENGTH_LONG).show();
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return id.size();
    }


//    public class ViewHolder extends RecyclerView.ViewHolder{
//        public TextView courseTV;
//        private int position;
//        private ImageView courseIV;
//        public CardView cardView;
//        public ClipData.Item currentItem;
//        public View view;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            courseTV = itemView.findViewById(R.id.description);
//            courseIV = itemView.findViewById(R.id.title);
//            cardView = itemView.findViewById(R.id.cv_id2);
//
//
//
//
//        }
//
//
//
//
//
//    }


}
