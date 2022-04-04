package com.linda.food.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.linda.food.Constants.Constants;
import com.linda.food.R;
import com.linda.food.models.Food;

import java.util.List;

public class CheckoutRecyclerAdapter extends RecyclerView.Adapter<CheckoutRecyclerAdapter.CheckoutViewHolder> {
    Context context;
    List<Food> foodList;

    public CheckoutRecyclerAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public CheckoutRecyclerAdapter.CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CheckoutRecyclerAdapter.CheckoutViewHolder(LayoutInflater.from(context).inflate(R.layout.individual_checkout_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutRecyclerAdapter.CheckoutViewHolder holder, int position) {
        final Food food = foodList.get(position);
        holder.bind(food);
    }

    @Override
    public int getItemCount() {
        return foodList.size() ;
    }

    public static final class CheckoutViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        TextView quantity;
        ImageView increase;
        ImageView decrease;
        int myQuantity = 1;
        private SharedPreferences sharedPreferences;
        private SharedPreferences.Editor editor;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            foodImage = itemView.findViewById(R.id.food_image);
            quantity = itemView.findViewById(R.id.food_quantity);
            increase = itemView.findViewById(R.id.plus);
            decrease = itemView.findViewById(R.id.minus);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
            editor = sharedPreferences.edit();
        }

        public void bind(Food food) {
            foodName.setText(food.getFoodName());
            foodPrice.setText("Ksh. "+String.valueOf(food.getFoodPrice()));
            Glide.with(itemView.getContext()).load(food.getFoodImgUrl()).transform(new RoundedCorners(20)).centerCrop().into(foodImage);

            increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myQuantity++;
                    food.setQuantity(myQuantity);
                    quantity.setText(String.valueOf(myQuantity));
                    addQuantityToSharedPreferences(myQuantity);
                    //CartViewHolder.this.notify();
                }
            });

            decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myQuantity>1){
                        myQuantity--;
                        food.setQuantity(myQuantity);
                        quantity.setText(String.valueOf(myQuantity));
                        addQuantityToSharedPreferences(myQuantity);
                        //CartViewHolder.this.notify();
                    }
                    else{
                        Toast.makeText(itemView.getContext(),"Sorry, you can't by less than one item! ", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        //Add order to sharedpreferences
        private void addQuantityToSharedPreferences(int quantity){
            editor.putInt(String.valueOf(Constants.PREFERENCES_QUANTITY),quantity).apply();
        }
    }
}
