package com.linda.food.UI;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.linda.food.Constants.Constants;
import com.linda.food.R;
import com.linda.food.adapters.FavoritesRecyclerAdapter;
import com.linda.food.models.Food;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Favorites extends AppCompatActivity {

    @BindView(R.id.foodRecyclerFood)
    RecyclerView recyclerView;
    @BindView(R.id.noProducts)
    LinearLayout noFaves;

   DatabaseReference databaseReference;
   List<Food> foodList;
   FavoritesRecyclerAdapter recyclerAdapter;
    private ProgressDialog dialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        userId = sharedPreferences.getString(Constants.PREFERENCES_FIREBASE_USER_ID,null);

        databaseReference = FirebaseDatabase.getInstance().getReference("Favorites").child(userId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        foodList = new ArrayList<>();
        recyclerAdapter = new FavoritesRecyclerAdapter(foodList,this);
        recyclerView.setAdapter(recyclerAdapter);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please Wait....");
        dialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Food food = dataSnapshot.getValue(Food.class);
                    foodList.add(food);
                    System.out.println(foodList);
                }
                if (foodList.size()>0) {
                    recyclerAdapter.notifyDataSetChanged();
                }
                else {
                    noFaves.setVisibility(View.VISIBLE);
                }
                dialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }


}