package com.linda.food.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.linda.food.R;
import com.linda.food.adapters.CheckoutRecyclerAdapter;
import com.linda.food.models.Food;
import com.linda.food.models.PrefConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Checkout extends AppCompatActivity {
    @BindView(R.id.checkout) Button checkout;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.failure) TextView failure;
    CheckoutRecyclerAdapter checkoutRecyclerAdapter;
    List<Food> cartFoodList;
    private DatabaseReference order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);

        cartFoodList = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        order = FirebaseDatabase.getInstance().getReference("Orders").child(userId);

        if (cartFoodList == null){
            showEmptyCartMessage();
        }else {
            cartFoodList = PrefConfig.readListFromPref(getApplicationContext());
            setCheckoutRecycler(cartFoodList);
        }
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Food food: cartFoodList){
                    saveOrderToFirebase(food.getId(),food.getFoodName(),food.getFoodRating(),food.getFoodImgUrl(),food.getRestaurant(),food.getFoodPrice());
                }
                startActivity(new Intent(Checkout.this, Summary.class));
            }
        });



    }

    private void setCheckoutRecycler(List<Food> foodList){
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        checkoutRecyclerAdapter = new CheckoutRecyclerAdapter(this,foodList);
        recyclerView.setAdapter(checkoutRecyclerAdapter);
    }
    private void saveOrderToFirebase(String food_id, String food_name, int food_rating, String food_image, String restaurant_id, int food_price ){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        Food myFood = new Food();
        myFood.setFoodName(food_name);
        myFood.setId(food_id);
        myFood.setFoodRating(food_rating);
        myFood.setFoodImgUrl(food_image);
        myFood.setRestaurant(restaurant_id);
        myFood.setFoodPrice(food_price);
        order.push().setValue(myFood);
        Toast.makeText(Checkout.this, "Your Order Has Been Made", Toast.LENGTH_LONG).show();
    }
    private void showEmptyCartMessage(){
        failure.setText("You haven't added any item here!");
        failure.setVisibility(View.VISIBLE);
    }
}