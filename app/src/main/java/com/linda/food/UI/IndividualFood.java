package com.linda.food.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.linda.food.Constants.Constants;
import com.linda.food.Network.FoodzillaClient;
import com.linda.food.Network.FoodzillaService;
import com.linda.food.R;
import com.linda.food.models.Food;
import com.linda.food.models.PrefConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndividualFood extends AppCompatActivity {

    @BindView(R.id.foodImage) ImageView foodImage;
    @BindView(R.id.foodName) TextView foodName;
    @BindView(R.id.foodPrice) TextView foodPrice;
    @BindView(R.id.foodDescription) TextView foodDescription;
    @BindView(R.id.ratingBar) RatingBar ratingBar;
    @BindView(R.id.addToCart) Button addToCart;

    List<Food> cartFoodList;
    private SharedPreferences sharedPreferences;
    String foodId;
    Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_food);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cartFoodList = new ArrayList<>();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(IndividualFood.this);
        foodId = sharedPreferences.getString(Constants.PREFERENCES_FOOD_ID,null);

        FoodzillaService service = FoodzillaClient.getClient();
        Call<Food> foodCall = service.getFoodById(foodId);
        foodCall.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (response.isSuccessful()){
                    food = response.body();
                    Glide.with(getApplicationContext()).load(food.getFoodImgUrl()).transform(new RoundedCorners(20)).centerCrop().into(foodImage);
                    foodName.setText(food.getFoodName());
                    foodPrice.append(String.valueOf(food.getFoodPrice()));
                    ratingBar.setRating(food.getFoodRating());
                    foodDescription.setText(food.getFoodDescription());

                }
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {

            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food.setAddedToCart(true);
                Toast.makeText(IndividualFood.this, "Added to Cart", Toast.LENGTH_LONG).show();
                // Snackbar.make(addToCart,"Added to Cart",Snackbar.LENGTH_SHORT).setBackgroundTint(Color.BLACK).show();
                cartFoodList.add(food);
                PrefConfig.writeListInPref(getApplicationContext(),cartFoodList);
            }
        });
    }
}