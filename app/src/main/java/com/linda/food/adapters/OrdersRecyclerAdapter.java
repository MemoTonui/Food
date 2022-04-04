package com.linda.food.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.linda.food.R;
import com.linda.food.models.Food;
import com.linda.food.models.Orders;

import java.util.List;

public class OrdersRecyclerAdapter extends RecyclerView.Adapter<OrdersRecyclerAdapter.OrdersViewHolder> {
    Context context;
    List<Orders> foodList;

    public OrdersRecyclerAdapter(Context context, List<Orders> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrdersRecyclerAdapter.OrdersViewHolder(LayoutInflater.from(context).inflate(R.layout.individual_order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        Orders orders = foodList.get(position);
        holder.bind(orders);
    }

    @Override
    public int getItemCount() {
        if (foodList.size()!=0){
            return foodList.size();
        }
        return 0;
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        RatingBar rating;
        TextView foodStatus;
        TextView time;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            foodImage = itemView.findViewById(R.id.food_image);
            foodStatus = itemView.findViewById(R.id.food_status);
            rating = itemView.findViewById(R.id.ratingBar);
            time = itemView.findViewById(R.id.time);
        }

        public void bind(Orders orders) {
            for (Food food:orders.getFood()) {
                foodName.setText(food.getFoodName());
                foodPrice.setText("Ksh. "+String.valueOf(food.getFoodPrice()));
                rating.setRating(food.getFoodRating());
                if (orders.getStatus() == "Pending"){
                    foodStatus.setText("Pending");
                }
                else if (orders.getStatus() == "Delivered"){
                    foodStatus.setText("Delivered");
                    foodStatus.setTextColor(Color.parseColor("#008000"));
                }

                time.setText(String.valueOf(orders.getDate()));
                Glide.with(itemView.getContext()).load(food.getFoodImgUrl()).transform(new RoundedCorners(20)).centerCrop().into(foodImage);
            }

        }
    }
}
