package com.linda.food.adapters;

import android.content.Context;
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
import com.linda.food.R;
import com.linda.food.models.Food;
import com.linda.food.models.PrefConfig;

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
        if (foodList!= null) {
            if (foodList.size() > 0) {
                return foodList.size();
            }
        }
        return 0;
    }

    public static final class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        TextView quantity;
        ImageView increase;
        ImageView decrease;
        ImageView delete;
        int myQuantity = 1;
        List<Food> cartFoodList;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            foodImage = itemView.findViewById(R.id.food_image);
            quantity = itemView.findViewById(R.id.quantity);
            increase = itemView.findViewById(R.id.plus);
            decrease = itemView.findViewById(R.id.minus);
            delete = itemView.findViewById(R.id.delete);

            cartFoodList = PrefConfig.readListFromPref(itemView.getContext());
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
                        //CartViewHolder.this.notify();
                    }
                    else{
                        Toast.makeText(itemView.getContext(),"Sorry, you can't by less than one item! ", Toast.LENGTH_LONG).show();
                    }
                }
            });
            quantity.setText(String.valueOf(myQuantity));

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrefConfig.deleteInPref(itemView.getContext(),food);
                    Toast.makeText(itemView.getContext(),"Deleted", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    }
