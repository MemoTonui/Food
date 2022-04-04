        package com.linda.food.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;



@Generated("jsonschema2pojo")
public class Restaurant implements Serializable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("restaurantName")
    @Expose
    private String restaurantName;
    @SerializedName("restaurantLocation")
    @Expose
    private String restaurantLocation;
    @SerializedName("restaurantRating")
    @Expose
    private Integer restaurantRating;
    @SerializedName("restaurantStatus")
    @Expose
    private String restaurantStatus;
    @SerializedName("restaurantImgUrl")
    @Expose
    private String restaurantImgUrl;
    private String restaurantLongitude;
    private String restaurantLatitude;
    @SerializedName("foods")
    @Expose
    private List<Object> foods = null;
    @SerializedName("__v")
    @Expose
    private Integer v;
    private final static long serialVersionUID = -5358242780990671471L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Restaurant() {
    }

    /**
     *
     * @param foods
     * @param restaurantName
     * @param restaurantLocation
     * @param restaurantRating
     * @param v
     * @param id
     * @param restaurantStatus
     * @param restaurantImgUrl
     */
    public Restaurant(String id, String restaurantName, String restaurantLocation, Integer restaurantRating, String restaurantStatus, String restaurantImgUrl, List<Object> foods, Integer v) {
        super();
        this.id = id;
        this.restaurantName = restaurantName;
        this.restaurantLocation = restaurantLocation;
        this.restaurantRating = restaurantRating;
        this.restaurantStatus = restaurantStatus;
        this.restaurantImgUrl = restaurantImgUrl;
        this.foods = foods;
        this.v = v;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantLocation() {
        return restaurantLocation;
    }

    public void setRestaurantLocation(String restaurantLocation) {
        this.restaurantLocation = restaurantLocation;
    }

    public Integer getRestaurantRating() {
        return restaurantRating;
    }

    public void setRestaurantRating(Integer restaurantRating) {
        this.restaurantRating = restaurantRating;
    }

    public String getRestaurantStatus() {
        return restaurantStatus;
    }

    public void setRestaurantStatus(String restaurantStatus) {
        this.restaurantStatus = restaurantStatus;
    }

    public String getRestaurantImgUrl() {
        return restaurantImgUrl;
    }

    public void setRestaurantImgUrl(String restaurantImgUrl) {
        this.restaurantImgUrl = restaurantImgUrl;
    }

    public List<Object> getFoods() {
        return foods;
    }

    public void setFoods(List<Object> foods) {
        this.foods = foods;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getRestaurantLongitude() {
        return restaurantLongitude;
    }

    public void setRestaurantLongitude(String restaurant_longitude) {
        this.restaurantLongitude = restaurant_longitude;
    }

    public String getRestaurantLatitude() {
        return restaurantLatitude;
    }

    public void setRestaurantLatitude(String restaurant_latitude) {
        this.restaurantLatitude = restaurant_latitude;
    }
}