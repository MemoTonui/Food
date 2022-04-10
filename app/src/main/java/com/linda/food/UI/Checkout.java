package com.linda.food.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.linda.food.Constants.Constants;
import com.linda.food.Network.FoodzillaClient;
import com.linda.food.Network.FoodzillaService;
import com.linda.food.R;
import com.linda.food.adapters.CheckoutRecyclerAdapter;
import com.linda.food.models.Food;
import com.linda.food.models.Orders;
import com.linda.food.models.PrefConfig;
import com.linda.food.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Checkout extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.checkout) Button checkout;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.failure) TextView failure;
    @BindView(R.id.location) TextInputLayout location;
    @BindView(R.id.subTotal) TextView subTotal;
    @BindView(R.id.total) TextView total;
    @BindView(R.id.delivery) TextView myDelivery;
    @BindView(R.id.picLocation) AutoCompleteTextView pickLocation;
    @BindView(R.id.linearLayout2) LinearLayout theDelivery;
    CheckoutRecyclerAdapter checkoutRecyclerAdapter;
    List<Food> cartFoodList;
    private static final String TAG = Checkout.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressDialog dialog;


    String[] locations;
    ArrayAdapter arrayAdapter;

    int sub = 0;
    int delivery;
    int tot;
    int quantity;
    String restaurantId;
    String id;
    String myLocation="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cartFoodList = new ArrayList<>();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();



        restaurantId = sharedPreferences.getString(Constants.PREFERENCES_RESTAURANT_ID,null);
        id = sharedPreferences.getString(Constants.PREFERENCES_USER_ID, null);
        quantity = sharedPreferences.getInt(String.valueOf(Constants.PREFERENCES_QUANTITY),0);

        locations = getResources().getStringArray(R.array.pick_up_location);
        arrayAdapter = new ArrayAdapter(this,R.layout.dropdown_item,locations);
        pickLocation.setAdapter(arrayAdapter);



        getRestaurantById(restaurantId);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        if (cartFoodList == null) {
            showEmptyCartMessage();
        } else {
            cartFoodList = PrefConfig.readListFromPref(getApplicationContext());
            setCheckoutRecycler(cartFoodList);
        }


       for(Food food: cartFoodList){
           sub+=food.getFoodPrice()*quantity;


       }



        //sub = cartFoodList.get();
        subTotal.setText("Ksh "+String.valueOf(sub));

        pickLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myLocation = parent.getAdapter().getItem(position).toString();
                if (myLocation.equals("Kawangware")) {
                    delivery = 200;
                } else if (myLocation.equals("Ngong Road")){
                    delivery = 150;
                } else if (myLocation.equals("Nairobi CBD")){
                    delivery = 50;
                } else if (myLocation.equals("Ongata Rongai")){
                    delivery = 300;
                } else if (myLocation.equals("South B")){
                    delivery = 100;
                } else if (myLocation.equals("Kiambu")){
                    delivery = 300;
                }
                else {
                    delivery = 216;
                }

                theDelivery.setVisibility(View.VISIBLE);
                myDelivery.setText("Ksh "+ String.valueOf(delivery));
                addDeliveryCostToSharedPreferences(delivery);

                tot = sub + delivery;
                total.setVisibility(View.VISIBLE);
                total.setText("Ksh "+String.valueOf(tot));

            }
        });


        checkout.setOnClickListener(this);
    }

    private void setCheckoutRecycler(List<Food> foodList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        checkoutRecyclerAdapter = new CheckoutRecyclerAdapter(this, foodList);
        recyclerView.setAdapter(checkoutRecyclerAdapter);
    }

    private void saveOrder() {
        com.linda.food.models.Orders orders = new com.linda.food.models.Orders();
        orders.setSubTotal(sub);
        orders.setFood(cartFoodList);
        orders.setUser(id);
        orders.setLocation(myLocation);
        orders.setStatus("Pending");
        orders.setDeliveryCost(delivery);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please Wait....");
        dialog.show();

        FoodzillaService foodzillaService = FoodzillaClient.getClient();
        Call<com.linda.food.models.Orders> ordersCall = foodzillaService.makeAnOrder(id,orders);
        ordersCall.enqueue(new Callback<Orders>() {
          @Override
          public void onResponse(Call<Orders> call, Response<Orders> response) {
              if (response.isSuccessful()){
                  assert response.body() != null;
                  addOrderToSharedPreferences(response.body().getId());
                  System.out.println("HEERRREEEEEEEEEEEEEEEEEEEE"+response.body().getId());

                  PrefConfig.ClearInPref(getApplicationContext(),cartFoodList);
                  Toast.makeText(Checkout.this, "Your Order Has Been Made", Toast.LENGTH_LONG).show();
                  startActivity(new Intent(Checkout.this,Success.class));
                  dialog.hide();
              }
              else {
                  dialog.hide();
                  Toast.makeText(Checkout.this, "Sorry, we are unable to make this order at this time. Please try again later", Toast.LENGTH_LONG).show();
                  startActivity(new Intent(Checkout.this,FailedActivity.class));

              }
          }
          @Override
          public void onFailure(Call<Orders> call, Throwable t) {
              dialog.hide();
              Toast.makeText(Checkout.this, "Your Order Has Been Made", Toast.LENGTH_LONG).show();
              startActivity(new Intent(Checkout.this,Success.class));
              PrefConfig.ClearInPref(getApplicationContext(),cartFoodList);

          }
      });
    }

    private void showEmptyCartMessage() {
        failure.setText("You haven't added any item here!");
        failure.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v == checkout) {
            saveOrder();
            sub = 0;
           /* for (Food food : cartFoodList) {
                //saveOrderToFirebase(food.getId(), food.getFoodName(), food.getFoodRating(), food.getFoodImgUrl(), food.getRestaurant(), food.getFoodPrice(), food.getQuantity(), sub, delivery, tot, myLatitude,myLongitude);
            }*/
          //  startActivity(new Intent(Checkout.this, Success.class));

        }
    }

    //Add order to sharedpreferences
    private void addOrderToSharedPreferences(String orderId){
        editor.putString(Constants.PREFERENCES_ORDER_ID,orderId).apply();
    }
    private void addDeliveryCostToSharedPreferences(int delivery){
        editor.putInt(String.valueOf(Constants.PREFERENCES_DELIVERY),delivery).apply();
    }

    private void getRestaurantById(String id){
        FoodzillaService service = FoodzillaClient.getClient();
        Call<Restaurant> restaurantCall = service.findRestaurantById(id);
        restaurantCall.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if(response.isSuccessful()){
                    Restaurant restaurant = response.body();
                    System.out.println("HEEEEEEEEEEEEREEEEEEE" + response.body().getRestaurantName());
                  /* if (restaurant.getRestaurantLocation() == null){
                       startLatitude = 0.3031;
                   }else {
                       startLatitude = Double.valueOf(restaurant.getRestaurantLatitude());
                   }
                   if (restaurant.getRestaurantLongitude() == null){
                       startLongitude =36.0800;
                   }
                   else {
                       startLongitude = Double.valueOf(restaurant.getRestaurantLongitude());
                   }*/
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {

            }
        });
    }
}