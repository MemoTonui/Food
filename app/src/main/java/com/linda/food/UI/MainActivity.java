package com.linda.food.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.linda.food.Network.FoodzillaClient;
import com.linda.food.Network.FoodzillaService;
import com.linda.food.R;
import com.linda.food.adapters.RestaurantRecyclerAdapter;
import com.linda.food.models.Restaurant;

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
    private static final String TAG = MainActivity.class.getSimpleName();

    List<Restaurant> restaurants = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Gets name of the current user of the application
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            //name.setText();
            name.setText("Hello"+ firebaseUser.getDisplayName()+"&#128522;");
        }
       /* authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    //name.setText();
                    name.setText("Hello"+ firebaseUser.getDisplayName()+"&#128522;");
                }
            }
        };*/

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


        //Implementation of Yelp Service
        FoodzillaService client = FoodzillaClient.getClient();
        Call<List<Restaurant>> call =client.getAllRestaurants();



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
        Intent intent = new Intent(MainActivity.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
                break;
            case R.id.nav_support:
                startActivity(new Intent(MainActivity.this,Support.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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