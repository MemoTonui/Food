package com.linda.food.Network;

import com.linda.food.models.Food;
import com.linda.food.models.Restaurant;
import com.linda.food.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FoodzillaService {

    @GET("/restaurants/")
    Call<List<Restaurant>> getAllRestaurants();

    @GET("/restaurants/{id}/food/")
    Call<List<Food>> getFoodsInARestaurant(@Path("id") String id);

    @POST("/users/")
    Call<User> createUser(@Body User user);

    @GET("/users/{id}")
    Call<User> getUser(@Path("id") String id);

    @PATCH("/users/{id}")
    Call<User> updateUser(@Path("id") String id, @Body User user);


   /* @GET("restaurants")
    Call<List<Business>> getAllBusinesses();

    @GET("restaurant/{restaurant_id}/food")
    Call<List<Food>> getFoodsInARestaurant(
    @Path("restaurant_id") int restaurant_id
    );*/
}
