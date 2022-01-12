package com.linda.food.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.linda.food.R;
import com.linda.food.models.Food;

public class FirebasFavoritesViewHolder extends RecyclerView.ViewHolder {
    ImageView foodImage;
    TextView foodName;
    TextView foodPrice;
    RatingBar rating;
    public FirebasFavoritesViewHolder(@NonNull View itemView) {
        super(itemView);
        foodName = itemView.findViewById(R.id.food_name);
        foodPrice = itemView.findViewById(R.id.food_price);
        foodImage = itemView.findViewById(R.id.food_image);
        rating = itemView.findViewById(R.id.ratingBar);
    }
    public void bind(Food food) {
        foodName.setText(food.getFoodName());
        foodPrice.setText(String.valueOf(food.getFoodPrice()));
        rating.setRating(food.getFoodRating());
        Glide.with(itemView.getContext()).load(food.getFoodImgUrl()).transform(new RoundedCorners(20)).centerCrop().into(foodImage);

    }
}
