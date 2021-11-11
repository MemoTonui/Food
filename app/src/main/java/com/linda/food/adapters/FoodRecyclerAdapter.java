package com.linda.food.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linda.food.R;
import com.linda.food.models.Food;

import java.util.List;

public class FoodRecyclerAdapter extends RecyclerView.Adapter<FoodRecyclerAdapter.FoodViewHolder> {

    Context context;
    List<Food>foods;

    public FoodRecyclerAdapter(Context context, List<Food> foods) {
        this.context = context;
        this.foods = foods;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodRecyclerAdapter.FoodViewHolder(LayoutInflater.from(context).inflate(R.layout.individual_food,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public static final class FoodViewHolder extends RecyclerView.ViewHolder  {
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        RatingBar rating;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            foodImage = itemView.findViewById(R.id.food_image);
            rating = itemView.findViewById(R.id.rating);




        }


    }
}
