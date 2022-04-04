package com.linda.food.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.linda.food.Constants.Constants;
import com.linda.food.Network.FoodzillaClient;
import com.linda.food.Network.FoodzillaService;
import com.linda.food.R;
import com.linda.food.adapters.RestaurantRecyclerAdapter;
import com.linda.food.models.Restaurant;
import com.linda.food.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RestaurantRecyclerAdapter recyclerAdapter;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    @BindView(R.id.restaurantsRecyclerView) RecyclerView recyclerView;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.failure) TextView failure;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView (R.id.cart)
    ImageView cart;
    @BindView(R.id.favorites) ImageView favorites;
    @BindView(R.id.search)
    EditText search;

    private static final String TAG = MainActivity.class.getSimpleName();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    String id;

    List<Restaurant> restaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        editor = sharedPreferences.edit();

        id = sharedPreferences.getString(Constants.PREFERENCES_USER_ID,null);

        getUser();
        getAllRestaurants();

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Cart.class);
                startActivity(intent);
            }
        });

        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Favorites.class);
                startActivity(intent);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


        //Tool bar
        setSupportActionBar(toolbar);

        //Navigation Drawer Menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav_drawer,R.string.close_nav_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //On item selected listener
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);


    }

    private void  filter(String text){
         ArrayList<Restaurant> filteredList = new ArrayList<>();

         for (Restaurant restaurant : restaurants){
             if (restaurant.getRestaurantName().toLowerCase().contains(text.toLowerCase())){
                 filteredList.add(restaurant);
             }
         }
         recyclerAdapter.filterList(filteredList);
    }
    private void getAllRestaurants(){
        FoodzillaService client = FoodzillaClient.getClient();
        Call<List<Restaurant>> call = client.getAllRestaurants();

        call.enqueue(new Callback<List<com.linda.food.models.Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if (response.isSuccessful()){
                    hideProgressBar();
                    List<Restaurant> body = response.body();
                    if(body == null){
                        Log.d(TAG,"Hapa Hakuna Kitu") ;
                        failure.setText("There are no restaurants here");
                        return;
                    }else {
                        restaurants.addAll(body);
                        recyclerAdapter = new RestaurantRecyclerAdapter(MainActivity.this, restaurants);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setAdapter(recyclerAdapter);

                    }
                }
                else {
                    hideProgressBar();
                    showUnSuccessfulMessage();
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                hideProgressBar();
                showFailureMessage();
                Log.d(TAG,"On Failure",t);
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }


    //Logout method
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private  void getUser(){
        FoodzillaService service = FoodzillaClient.getClient();
        Call<User> userCall = service.getUser(id);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    User user = response.body();
                    name.setText(user.getFullName());
                    ImageView profile = navigationView.findViewById(R.id.imageView5);
                    TextView username = navigationView.findViewById(R.id.username);
                    TextView email = navigationView.findViewById(R.id.email);

                    Glide.with(getApplicationContext()).load(user.getImgUrl()).apply(RequestOptions.circleCropTransform()).into(profile);
                    username.setText(user.getUsername());
                    email.setText(user.getEmail());

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    //Navigation using the menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_orders:
                startActivity(new Intent(MainActivity.this,Orders.class));
                break;
            case R.id.nav_favorites:
                startActivity(new Intent(MainActivity.this,Favorites.class));
                break;
            case R.id.nav_cart:
                startActivity(new Intent(MainActivity.this,Cart.class));
                break;
            case R.id.nav_profile:
                startActivity(new Intent(MainActivity.this,Profile.class));
                break;
            case R.id.nav_logout:
                logout();
                deleteUser();
                break;
            case R.id.nav_support:
                startActivity(new Intent(MainActivity.this,Support.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    
    private void deleteUser(){
        FoodzillaService service = FoodzillaClient.getClient();
        Call<Object> call = service.deleteUser(id);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Account Deleted",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }
    //Progress Bar
    private  void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
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