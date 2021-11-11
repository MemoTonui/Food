package com.linda.food.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linda.food.R;
import com.linda.food.UI.FoodActivity;
import com.linda.food.models.Restaurants;

import java.util.List;

public class RestaurantRecyclerAdapter  extends RecyclerView.Adapter<RestaurantRecyclerAdapter.RestaurantsViewHolder> {

    Context context;
    List<Restaurants> restaurants;

    public RestaurantRecyclerAdapter(Context context, List<Restaurants> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    public static final class RestaurantsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView restaurantImage;
        TextView restaurantName;
        RatingBar rating;

        public RestaurantsViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantImage = itemView.findViewById(R.id.restaurantImage);
            restaurantName = itemView.findViewById(R.id.restaurantName);
            rating = itemView.findViewById(R.id.rating);
            restaurantImage.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if(v==restaurantImage ){
                Intent intent = new Intent(itemView.getContext(), FoodActivity.class);
                itemView.getContext().startActivity(intent);

            }
        }
    }
    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestaurantsViewHolder(LayoutInflater.from(context).inflate(R.layout.individual_restaurant,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder holder, int position) {
        //holder.restaurantImage.setText(restaurants.get(position).getRestaurant_image());
        //holder.restaurantName.setText(restaurants.get(position).getRestaurant_name());
        //holder.rating.setRating(restaurants.get(position).getRestaurant_ratings());
    }



    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}
