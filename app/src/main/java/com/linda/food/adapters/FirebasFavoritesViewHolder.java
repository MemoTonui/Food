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
        foodName.setText(food.getFood_name());
        foodPrice.setText(String.valueOf(food.getFood_price()));
        rating.setRating(food.getFood_rating());
        Glide.with(itemView.getContext()).load(food.getFood_image()).transform(new RoundedCorners(20)).centerCrop().into(foodImage);

    }
}
