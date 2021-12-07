package com.linda.food.UI;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    @BindView(R.id.failure)
    TextView failure;

   DatabaseReference databaseReference;
   List<Food> foodList;
   FavoritesRecyclerAdapter recyclerAdapter;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
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
                recyclerAdapter.notifyDataSetChanged();
                dialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }

    private void showEmptyCartMessage(){
        failure.setText("You haven't added any item here!");
        failure.setVisibility(View.VISIBLE);
    }
}