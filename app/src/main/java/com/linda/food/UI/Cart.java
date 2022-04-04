package com.linda.food.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linda.food.R;
import com.linda.food.adapters.CartRecyclerAdapter;
import com.linda.food.models.Food;
import com.linda.food.models.PrefConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Cart extends AppCompatActivity {

    @BindView(R.id.checkout) Button proceedToCheckout;
    CartRecyclerAdapter cartRecyclerAdapter;
    @BindView(R.id.failure) TextView failure;
   /* @BindView(R.id.progressBar)
    ProgressBar progressBar;*/
    @BindView(R.id.cartRecyclerView)RecyclerView recyclerView;
    List<Food> cartFoodList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        cartFoodList = new ArrayList<>();
        if (cartFoodList == null){
            cartRecyclerAdapter.notifyDataSetChanged();
            showEmptyCartMessage();
        }else {
            cartFoodList = PrefConfig.readListFromPref(getApplicationContext());
            setCartRecycler(cartFoodList);
        }
        proceedToCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cart.this,Checkout.class));
            }
        });


    }

   private void setCartRecycler(List<Food> foodList){
        recyclerView = findViewById(R.id.cartRecyclerView);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        cartRecyclerAdapter = new CartRecyclerAdapter(this,foodList);
        recyclerView.setAdapter(cartRecyclerAdapter);
    }

    //Progress Bar
   /* private  void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
    }*/

    private void showEmptyCartMessage(){
        failure.setText("You haven't added any item here!");
        failure.setVisibility(View.VISIBLE);
    }

}