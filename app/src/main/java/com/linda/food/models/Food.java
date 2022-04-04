
package com.linda.food.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Food implements Serializable
{

    @SerializedName("foodPrice")
    @Expose
    private Integer foodPrice;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("foodName")
    @Expose
    private String foodName;
    @SerializedName("foodDescription")
    @Expose
    private String foodDescription;
    @SerializedName("foodRating")
    @Expose
    private Integer foodRating;
    @SerializedName("foodImgUrl")
    @Expose
    private String foodImgUrl;
    @SerializedName("restaurant")
    @Expose
    private String restaurant;
    private Integer quantity;
    private Integer total;
    private Integer subTotal;
    private Integer deliveryCost;
    private String latitude;
    private String longitude;
    @SerializedName("__v")
    @Expose
    private Integer v;
    private Boolean isAddedToCart =false;
    private Boolean isAddedToFavorites = false;
    private final static long serialVersionUID = 5812465384064518323L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Food() {
    }

    /**
     *
     * @param foodPrice
     * @param foodRating
     * @param foodName
     * @param v
     * @param restaurant
     * @param foodImgUrl
     * @param id
     */
    public Food(Integer foodPrice, String id, String foodName, Integer foodRating, String foodImgUrl, String restaurant,Integer quantity, Integer v) {
        super();
        this.foodPrice = foodPrice;
        this.id = id;
        this.foodName = foodName;
        this.foodRating = foodRating;
        this.foodImgUrl = foodImgUrl;
        this.restaurant = restaurant;
        this.quantity = quantity;
        this.v = v;
    }

    public Integer getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(Integer foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Integer getFoodRating() {
        return foodRating;
    }

    public void setFoodRating(Integer foodRating) {
        this.foodRating = foodRating;
    }

    public String getFoodImgUrl() {
        return foodImgUrl;
    }

    public void setFoodImgUrl(String foodImgUrl) {
        this.foodImgUrl = foodImgUrl;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public Boolean getAddedToCart() {
        return isAddedToCart;
    }

    public void setAddedToCart(Boolean addedToCart) {
        isAddedToCart = addedToCart;
    }

    public Boolean getAddedToFavorites() {
        return isAddedToFavorites;
    }

    public void setAddedToFavorites(Boolean addedToFavorites) {
        isAddedToFavorites = addedToFavorites;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(Integer deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public Integer getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Integer subTotal) {
        this.subTotal = subTotal;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }
}