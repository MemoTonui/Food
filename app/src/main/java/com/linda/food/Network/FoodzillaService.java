package com.linda.food.Network;

import com.linda.food.models.Food;
import com.linda.food.models.Orders;
import com.linda.food.models.Restaurant;
import com.linda.food.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FoodzillaService {

    @GET("/restaurants/")
    Call<List<Restaurant>> getAllRestaurants();

    @GET("/restaurants/{id}/food/")
    Call<List<Food>> getFoodsInARestaurant(@Path("id") String id);

    @GET("/restaurants/{id}")
    Call<Restaurant> findRestaurantById(@Path("id") String id);

    @POST("/users/")
    Call<User> createUser(@Body User user);

    @GET("/users/{id}")
    Call<User> getUser(@Path("id") String id);

    @GET("/users/phone/{phoneNumber}")
    Call<User> getUserByPhoneNumber(@Path("phoneNumber") String phoneNumber);

    @PATCH("/users/{id}")
    Call<User> updateUser(@Path("id") String id, @Body User user);

    @POST("/users/{id}/order/new")
    Call<Orders> makeAnOrder(
            @Path("id") String id,
            @Body Orders orders);

    @GET ("/users/{id}/orders")
    Call<List<Orders>> getUsersOrders(
            @Path("id") String id
    );

    @GET("/foods/{id}")
    Call<Food> getFoodById(
            @Path("id") String foodId
    );

    @GET("/orders/{id}")
    Call<Orders> getOrderById(
            @Path("id") String id
    );

    //deelete user
    @DELETE("/users/{id}")
    Call<Object> deleteUser(
            @Path("id") String id);

   /* @GET("restaurants")
    Call<List<Business>> getAllBusinesses();

    @GET("restaurant/{restaurant_id}/food")
    Call<List<Food>> getFoodsInARestaurant(
    @Path("restaurant_id") int restaurant_id
    );*/
}
