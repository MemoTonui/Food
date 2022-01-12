package com.linda.food.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.linda.food.R;
import com.linda.food.models.Food;

import java.util.List;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder> {
    Context context;
    List<Food> foodList;

    public CartRecyclerAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartRecyclerAdapter.CartViewHolder(LayoutInflater.from(context).inflate(R.layout.individual_cart_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerAdapter.CartViewHolder holder, int position) {
        final Food food = foodList.get(position);
        holder.bind(food);
    }

    @Override
    public int getItemCount() {
        if (foodList.size()>0) {
            return foodList.size();
        }
        return 0;
    }

    public static final class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        TextView quantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            foodImage = itemView.findViewById(R.id.food_image);
            quantity = itemView.findViewById(R.id.quantity);
        }

        public void bind(Food food) {
            foodName.setText(food.getFoodName());
            foodPrice.setText(String.valueOf(food.getFoodPrice()));
            Glide.with(itemView.getContext()).load(food.getFoodPrice()).transform(new RoundedCorners(20)).centerCrop().into(foodImage);
        }
    }
    }
