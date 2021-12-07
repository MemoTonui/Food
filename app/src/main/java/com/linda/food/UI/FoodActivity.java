package com.linda.food.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.linda.food.Constants.Constants;
import com.linda.food.Network.FoodzillaClient;
import com.linda.food.Network.FoodzillaService;
import com.linda.food.R;
import com.linda.food.adapters.FoodRecyclerAdapter;
import com.linda.food.models.Food;
import com.linda.food.models.PrefConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodActivity extends AppCompatActivity {

    FoodRecyclerAdapter foodRecyclerAdapter;
    List<Food> foodList= new ArrayList<>();
    List<Food> cartFoodList;
    List<Food> favoritesFoodsLIst;

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.foodRecyclerFood) RecyclerView foodRecyclerView;
    @BindView(R.id.failure) TextView failure;
    @BindView(R.id.restaurant_rating)
    RatingBar ratingBar;

    @BindView(R.id.toolbar2)
    Toolbar toolbar;
    @BindView(R.id.addToCart) ImageView addToCart;
    private SharedPreferences sharedPreferences;
    int restaurant_id;
    float rating;

    String restaurantName;
    //Firebase
    private  DatabaseReference food;
    private DatabaseReference foodName;
    private DatabaseReference foodPrice;
    private DatabaseReference image;
    private DatabaseReference foodRating;



    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        ButterKnife.bind(this);
        cartFoodList = new ArrayList<>();
        favoritesFoodsLIst = new ArrayList<>();

        //Gets User id
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        food = FirebaseDatabase.getInstance().getReference("Favorites").child(userId);
        //Creates a node that stores favorites
        DatabaseReference pushRef = food.push();

        //Toolbar
        setSupportActionBar(toolbar);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(FoodActivity.this);
        rating = sharedPreferences.getFloat(Constants.PREFERENCES_RESTAURANT_RATING,-1);
        restaurantName = sharedPreferences.getString(Constants.PREFERENCES_RESTAURANT_NAME, null);
        restaurant_id= sharedPreferences.getInt(Constants.PREFERENCES_RESTAURANT_ID,-1);
        FoodzillaService client = FoodzillaClient.getClient();
        Call <List<Food>> call = client.getFoodsInARestaurant(restaurant_id);

        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if(response.isSuccessful()){
                    hideProgressBar();
                    List<Food> body = response.body();
                    if (body== null){
                        Log.d(TAG,"Hapa Hakuna Kitu") ;
                        failure.setText("There is no food here");
                        return;
                    }else{
                        foodList.addAll(body);
                        ratingBar.setRating(rating);
                        toolbar.setTitle(restaurantName);
                       // restaurant_name.setText(restaurantName);
                        foodRecyclerAdapter = new FoodRecyclerAdapter(FoodActivity.this, foodList, new FoodRecyclerAdapter.ClickAddToCartListener() {
                            @Override
                            public void onClickAddToCart(ImageView addToCart, Food food) {
                                food.setAddedToCart(true);
                                Toast.makeText(FoodActivity.this, "Added to Cart", Toast.LENGTH_LONG).show();
                                // Snackbar.make(addToCart,"Added to Cart",Snackbar.LENGTH_SHORT).setBackgroundTint(Color.BLACK).show();
                                foodRecyclerAdapter.notifyDataSetChanged();
                                cartFoodList.add(food);
                                PrefConfig.writeListInPref(getApplicationContext(),cartFoodList);
                            }
                        }, new FoodRecyclerAdapter.ClickAddToFavoritesListener() {
                            @Override
                            public void onClickAddToFavorites(ImageView addToFavorites, Food food) {
                               saveFavoriteToFirebase(food.getFood_id(),food.getFood_name(),food.getFood_rating(),food.getFood_image(),food.getRestaurant_id(),food.getFood_price());

                                food.setAddedToFavorites(true);
                                addToFavorites.setImageResource(R.drawable.ic_vector__1_);
                                foodRecyclerAdapter.notifyDataSetChanged();
                            }

                        });

                        foodRecyclerView.setAdapter(foodRecyclerAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FoodActivity.this);
                        foodRecyclerView.setLayoutManager(layoutManager);
                        foodRecyclerView.setHasFixedSize(true);
                    }
                }else{
                    hideProgressBar();
                    showUnSuccessfulMessage();
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                hideProgressBar();
                showFailureMessage();
                Log.d(TAG,"On Failure",t);
            }
        });

    }


    //Progress Bar
    private  void hideProgressBar(){
        progressBar.setVisibility(View.GONE);

    }
    private void saveFavoriteToFirebase(int food_id, String food_name, float food_rating, String food_image, int restaurant_id, int food_price ){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        Food myFood = new Food(food_id,food_name,food_rating,food_image,restaurant_id,food_price);

        food.push().setValue(myFood);
        Toast.makeText(FoodActivity.this, "Added to Favorites", Toast.LENGTH_LONG).show();





    }
    private void showUnSuccessfulMessage(){
        failure.setText("Sorry you do not have Internet Access! Please Try Again Later");
        failure.setVisibility(View.VISIBLE);
    }
    private void showFailureMessage(){
        failure.setText("Sorry, Our Services are Unavailable!");
        failure.setVisibility(View.VISIBLE);
    }
}