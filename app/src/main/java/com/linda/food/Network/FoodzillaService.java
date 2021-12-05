package com.linda.food.Network;

import com.linda.food.models.Business;
import com.linda.food.models.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FoodzillaService {
    @GET("restaurants")
    Call<List<Business>> getAllBusinesses();

    @GET("restaurant/{restaurant_id}/food")
    Call<List<Food>> getFoodsInARestaurant(
    @Path("restaurant_id") int restaurant_id
    );
}
