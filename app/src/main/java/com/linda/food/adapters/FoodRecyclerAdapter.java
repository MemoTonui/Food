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
import com.linda.food.UI.IndividualFood;
import com.linda.food.models.Food;

import java.util.List;

public class FoodRecyclerAdapter extends RecyclerView.Adapter<FoodRecyclerAdapter.FoodViewHolder> {

    Context context;
    List<Food>foods;
    private ClickAddToCartListener ClickAddToCartListener;
    private ClickAddToFavoritesListener ClickAddToFavoritesListener;

    //Add to cart listener
    public interface ClickAddToCartListener{
        void onClickAddToCart(ImageView addToCart, Food food);
    }
    //Add To Favorites Listener
    public interface ClickAddToFavoritesListener {
        void onClickAddToFavorites(ImageView addToFavorites, Food food);
    }


    public FoodRecyclerAdapter(Context context, List<Food> foods,ClickAddToCartListener clickAddToCartListener, ClickAddToFavoritesListener clickAddToFavoritesListener) {
        this.context = context;
        this.foods = foods;
        this.ClickAddToCartListener = clickAddToCartListener;
        this.ClickAddToFavoritesListener = clickAddToFavoritesListener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodRecyclerAdapter.FoodViewHolder(LayoutInflater.from(context).inflate(R.layout.individual_food,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        final Food food = foods.get(position);
        holder.bind(food);
        holder.foodCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(food.getAddedToCart() == false) {
                    ClickAddToCartListener.onClickAddToCart(holder.foodCart, food);

                }
            }
        });
        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!food.getAddedToFavorites()){
                    ClickAddToFavoritesListener.onClickAddToFavorites(holder.favorite,food);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if(foods.size()!=0){
            return foods.size();
        }
        return 0;
    }

    public static final class FoodViewHolder extends RecyclerView.ViewHolder  {
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        RatingBar rating;
        ImageView foodCart;
        ImageView favorite;
        private SharedPreferences sharedPreferences;
        private SharedPreferences.Editor editor;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            foodImage = itemView.findViewById(R.id.food_image);
            rating = itemView.findViewById(R.id.ratingBar);
            foodCart = itemView.findViewById(R.id.cart);
            favorite = itemView.findViewById(R.id.favorites);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
            editor = sharedPreferences.edit();


        }

        public void bind(Food food) {
            foodName.setText(food.getFoodName());
            foodPrice.setText("Ksh. "+String.valueOf(food.getFoodPrice()));
            rating.setRating(food.getFoodRating());
            Glide.with(itemView.getContext()).load(food.getFoodImgUrl()).transform(new RoundedCorners(20)).centerCrop().into(foodImage);
            if(food.getAddedToCart()==true){
                foodCart.setColorFilter(Color.rgb(220,220,220));
            }
            if(food.getAddedToFavorites() == true){
                favorite.setColorFilter(Color.rgb(220,0,0));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFoodIdToSharedPreferences(food.getId());
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), IndividualFood.class));
                }
            });

        }
        private void addFoodIdToSharedPreferences(String foodId){
            editor.putString(Constants.PREFERENCES_FOOD_ID,foodId).apply();
        }
    }
}
