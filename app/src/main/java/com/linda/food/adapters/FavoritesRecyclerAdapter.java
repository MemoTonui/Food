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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.linda.food.R;
import com.linda.food.models.Food;

import java.util.List;


public class FavoritesRecyclerAdapter extends RecyclerView.Adapter<FavoritesRecyclerAdapter.FavoritesViewHolder> {
    List<Food> foodList;
    Context context;


    public FavoritesRecyclerAdapter(List<Food> foodList, Context context) {
        this.foodList = foodList;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoritesRecyclerAdapter.FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoritesRecyclerAdapter.FavoritesViewHolder(LayoutInflater.from(context).inflate(R.layout.individual_favorite,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesRecyclerAdapter.FavoritesViewHolder holder, int position) {
        final Food food = foodList.get(position);
        holder.bind(food);
    }

    @Override
    public int getItemCount() {
        if(foodList.size()!=0){
            return foodList.size();
        }
        return 0;
    }

    public class FavoritesViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        RatingBar rating;

        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            foodImage = itemView.findViewById(R.id.food_image);
            rating = itemView.findViewById(R.id.ratingBar);

        }

        public void bind(Food food) {
            foodName.setText(food.getFoodName());
            foodPrice.setText("Ksh. "+String.valueOf(food.getFoodPrice()));
            rating.setRating(food.getFoodRating());
            Glide.with(itemView.getContext()).load(food.getFoodImgUrl()).transform(new RoundedCorners(20)).centerCrop().into(foodImage);

        }
    }
}
