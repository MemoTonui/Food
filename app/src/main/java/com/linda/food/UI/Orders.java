package com.linda.food.UI;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linda.food.Constants.Constants;
import com.linda.food.Network.FoodzillaClient;
import com.linda.food.Network.FoodzillaService;
import com.linda.food.R;
import com.linda.food.adapters.OrdersRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Orders extends AppCompatActivity {
    @BindView(R.id.foodRecyclerFood)
    RecyclerView recyclerView;
    @BindView(R.id.noOrders)
    LinearLayout noOrders;

    List<com.linda.food.models.Orders> foodList;
    OrdersRecyclerAdapter recyclerAdapter;
    private ProgressDialog dialog;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        //Shared preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        userId = sharedPreferences.getString(Constants.PREFERENCES_USER_ID,null);

       // databaseReference = FirebaseDatabase.getInstance().getReference("Orders").child(userId);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please Wait....");
        dialog.show();
        FoodzillaService service = FoodzillaClient.getClient();
       Call<List<com.linda.food.models.Orders>> ordersCall = service.getUsersOrders(userId);
       ordersCall.enqueue(new Callback<List<com.linda.food.models.Orders>>() {
           @Override
           public void onResponse(Call<List<com.linda.food.models.Orders>> call, Response<List<com.linda.food.models.Orders>> response) {
               if (response.isSuccessful()){
                   foodList = response.body();
                   if (foodList.size()>0){
                       recyclerAdapter = new OrdersRecyclerAdapter(Orders.this,foodList);
                       recyclerView.setHasFixedSize(true);
                       recyclerView.setLayoutManager(new LinearLayoutManager(Orders.this));
                       recyclerView.setAdapter(recyclerAdapter);
                   }
                   else {
                       noOrders.setVisibility(View.VISIBLE);
                   }
                   dialog.hide();
               }
           }

           @Override
           public void onFailure(Call<List<com.linda.food.models.Orders>> call, Throwable t) {

           }
       });

    }
}