package com.app.presidents;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    List<President> data;
    Context context;

    private static final String TAG = "RecyclerViewAdapter";

    public RecyclerViewAdapter(List<President> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.name.setText(data.get(position).getName());
        holder.date.setText(data.get(position).getDate());
        holder.country.setText(data.get(position).getCountry());
        Glide.with(this.context).load(data.get(position).getImageURL()).into(holder.image);

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddEdit.class);
                intent.putExtra("name", data.get(position).getName());
                context.startActivity(new Intent(intent));
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, date, country;
        ConstraintLayout itemLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.rcv_image);
            name = itemView.findViewById(R.id.rcv_name);
            date = itemView.findViewById(R.id.rcv_date);
            country = itemView.findViewById(R.id.rcv_country);
            itemLayout = itemView.findViewById(R.id.rcv_item);

        }
    }

    public void swapData(List<President> filtersList) {
        if (filtersList != null) {
            data.clear();
            data.addAll(filtersList);
            notifyDataSetChanged();
        }
    }
}
