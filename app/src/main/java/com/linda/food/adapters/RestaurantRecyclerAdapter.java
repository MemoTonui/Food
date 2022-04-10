package com.linda.food.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
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
import com.linda.food.Constants.Constants;
import com.linda.food.R;
import com.linda.food.UI.FoodActivity;
import com.linda.food.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantRecyclerAdapter  extends RecyclerView.Adapter<RestaurantsViewHolder> {

    Context context;
    List<Restaurant> restaurants;

    public RestaurantRecyclerAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestaurantsViewHolder(LayoutInflater.from(context).inflate(R.layout.individual_restaurant,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder holder, int position) {
        holder.bind(restaurants.get(position));
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public void filterList(ArrayList<Restaurant> filteredList){
        restaurants = filteredList;
        notifyDataSetChanged();
    }
}
final class RestaurantsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView restaurantImage;
    TextView restaurantName;
    TextView restaurantId;
    RatingBar rating;
    TextView restaurantStatus;
    String isClosed ="isOpen";
    int restaurant_id;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public RestaurantsViewHolder(@NonNull View itemView) {
        super(itemView);
        //Shared preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
        editor = sharedPreferences.edit();

        restaurantId =itemView.findViewById(R.id.restaurantId);
        restaurantImage = itemView.findViewById(R.id.restaurantImage);
        restaurantName = itemView.findViewById(R.id.restaurantName);
        rating = itemView.findViewById(R.id.rating);
        restaurantStatus = itemView.findViewById(R.id.restaurantStatus);
        restaurantImage.setOnClickListener(this);
    }

    public void bind(final Restaurant business) {
        //Picasso.get().load(business.getImage_url()).into(restaurantImage);
        Glide.with(itemView.getContext()).load(business.getRestaurantImgUrl()).transform(new RoundedCorners(20)).centerCrop().into(restaurantImage);
        restaurantName.setText(business.getRestaurantName());
        restaurantId.setText(String.valueOf(business.getId()));

        if (isClosed == business.getRestaurantStatus()){
            restaurantStatus.setText("Closed");
            restaurantStatus.setTextColor(Color.rgb(200,0,0));
        }else{
            restaurantStatus.setText("Open");
            restaurantStatus.setTextColor(Color.rgb(0,200,100));
        }
        rating.setRating(business.getRestaurantRating());
    }
    //Add restaurant id to sharedpreferences
    private void addRestaurantToSharedPreferences(String restaurant_id){
        editor.putString(Constants.PREFERENCES_RESTAURANT_ID,restaurant_id).apply();
    }

    //Add restaurant name to sharedpreferences
    private void addRestaurantNameToSharedPreferences(String restaurant_name){
        editor.putString(Constants.PREFERENCES_RESTAURANT_NAME,restaurant_name).apply();
    }

    //Add restaurant rating to sharedpreferences
    private void addRestaurantRatingToSharedPreferences(int rating){
        editor.putInt(Constants.PREFERENCES_RESTAURANT_RATING,rating).apply();
    }

    //Add restaurant image url to sharedpreferences
    private void addRestaurantImageToSharedPreferences(String restaurant_image){
        editor.putString(Constants.PREFERENCES_RESTAURANT_IMAGE,restaurant_image).apply();
    }
    @Override
    public void onClick(View v) {
        if (v == restaurantImage) {
            Intent intent = new Intent(itemView.getContext(), FoodActivity.class);
            addRestaurantToSharedPreferences(restaurantId.getText().toString());
            addRestaurantNameToSharedPreferences(restaurantName.getText().toString());
            addRestaurantRatingToSharedPreferences((int) rating.getRating());
            itemView.getContext().startActivity(intent);
        }
    }
}
