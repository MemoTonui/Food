package com.linda.food.UI;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linda.food.R;
import com.linda.food.adapters.CartRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {
    RecyclerView recyclerView;
    Button proceedToCheckout;
    CartRecyclerAdapter cartRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        List<com.linda.food.models.Cart> foodList = new ArrayList<>();
        foodList.add(new com.linda.food.models.Cart("Lasagna",2000,3));
        setCartRecycler(foodList);
        proceedToCheckout = findViewById(R.id.checkout);

    }
    private void setCartRecycler(List<com.linda.food.models.Cart> foodList){
        recyclerView = findViewById(R.id.cartRecyclerView);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        cartRecyclerAdapter = new CartRecyclerAdapter(this,foodList);
        recyclerView.setAdapter(cartRecyclerAdapter);
    }
}