package com.example.hotelbooking;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class accountAdapter extends RecyclerView.Adapter<MyViewHolder1> {

    private Context context;
    private List<accountDataClass> dataList1;

    public accountAdapter(Context context, List<accountDataClass> dataList){
        this.context = context;
        this.dataList1 = dataList;


    }
    @NonNull
    @Override
    public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_account_item, parent, false);
        return new MyViewHolder1(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder1 holder, int position) {
        int nights = dataList1.get(position).getNights();
        String nightsText = nights + " night" + (nights > 1 ? "s" : "");
        holder.recNights.setText(nightsText);

        int totalAmount = dataList1.get(position).getTotalAmount();
        String formattedTotalAmount = totalAmount + "$";
        holder.recTotalAmount.setText(formattedTotalAmount);

        holder.recBookingDates.setText(dataList1.get(position).getBookingDate());
        holder.recHotelName.setText(dataList1.get(position).getHotelName());
        holder.recCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }



    @Override
    public int getItemCount() {
        return dataList1.size();
    }
}
class MyViewHolder1 extends RecyclerView.ViewHolder{

    TextView recNights,recTotalAmount,recBookingDates, recHotelName;
    CardView recCard1;
    public MyViewHolder1(@NonNull View itemView) {
        super(itemView);
        recNights = itemView.findViewById(R.id.recNights);
        recTotalAmount = itemView.findViewById(R.id.recTotalAmount);
        recBookingDates = itemView.findViewById(R.id.recBookingDates);
        recHotelName = itemView.findViewById(R.id.recHotelName);
        recCard1 = itemView.findViewById(R.id.recCard1);
    }
}

