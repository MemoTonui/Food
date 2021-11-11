package com.linda.food.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.linda.food.R;
import com.linda.food.adapters.RestaurantRecyclerAdapter;
import com.linda.food.models.Restaurants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView restaurantRecyclerView;
    RestaurantRecyclerAdapter recyclerAdapter;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Gets name of the current user of the application
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    //name.setText();
                    name.setText("Hello"+ firebaseUser.getDisplayName()+"&#128522;");
                }
            }
        };

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


        //Dummy data ya the liist of restauranta
        List<Restaurants> restaurantsList = new ArrayList<>();
        restaurantsList.add(new Restaurants("Lavington Restaurant",5,"Madaraka","https://ibb.co/JyWcN5N","45.00","45.00"));
        restaurantsList.add(new Restaurants("Kileleshwa Restaurant",4,"Madaraka","https://ibb.co/pbNrFs8","45.00","45.00"));
        restaurantsList.add(new Restaurants("Mumbai Restaurant",2,"Madaraka","https://ibb.co/qjV2RFx","45.00","45.00"));
        restaurantsList.add(new Restaurants("Lavington Restaurant",5,"Madaraka","https://ibb.co/JyWcN5N","45.00","45.00"));
        restaurantsList.add(new Restaurants("Kileleshwa Restaurant",4,"Madaraka","https://ibb.co/pbNrFs8","45.00","45.00"));
        restaurantsList.add(new Restaurants("Mumbai Restaurant",2,"Madaraka","https://ibb.co/qjV2RFx","45.00","45.00"));
        restaurantsList.add(new Restaurants("Lavington Restaurant",5,"Madaraka","https://ibb.co/JyWcN5N","45.00","45.00"));
        restaurantsList.add(new Restaurants("Kileleshwa Restaurant",4,"Madaraka","https://ibb.co/pbNrFs8","45.00","45.00"));
        restaurantsList.add(new Restaurants("Mumbai Restaurant",2,"Madaraka","https://ibb.co/qjV2RFx","45.00","45.00"));

         setRestaurantsRecycler(restaurantsList);
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

    private void setRestaurantsRecycler(List<Restaurants> restaurantsList){

        restaurantRecyclerView = findViewById(R.id.restaurantsRecyclerView);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        restaurantRecyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RestaurantRecyclerAdapter(this,restaurantsList);
         restaurantRecyclerView.setAdapter(recyclerAdapter );
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
}