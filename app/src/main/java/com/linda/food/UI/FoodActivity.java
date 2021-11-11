package com.linda.food.UI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linda.food.R;
import com.linda.food.adapters.FoodRecyclerAdapter;
import com.linda.food.models.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends AppCompatActivity {
    RecyclerView foodRecyclerView;
    FoodRecyclerAdapter foodRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        List<Food> foodList = new ArrayList<>();
        foodList.add(new Food("Food Tamu",4,"https:gg.com",1,400));
        foodList.add(new Food("Food Tamu",4,"https:gg.com",1,400));
        foodList.add(new Food("Food Tamu",4,"https:gg.com",1,400));
        foodList.add(new Food("Food Tamu",4,"https:gg.com",1,400));
        foodList.add(new Food("Food Tamu",4,"https:gg.com",1,400));
        setFoodRecycler(foodList);

    }
    private void setFoodRecycler(List<Food> foodList){
        foodRecyclerView = findViewById(R.id.foodRecyclerFood);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        foodRecyclerView.setLayoutManager(layoutManager);
        foodRecyclerAdapter = new FoodRecyclerAdapter(this,foodList);
        foodRecyclerView.setAdapter(foodRecyclerAdapter);
    }
}