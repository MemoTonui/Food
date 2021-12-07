package com.linda.food.models;

public class Food {
    private int food_id;
    private String food_name;
    private float food_rating;
    private String food_image;
    private int restaurant_id;
    private int food_price;
    private Boolean isAddedToCart =false;
    private Boolean isAddedToFavorites = false;

    public Food(int food_id, String food_name, float food_rating, String food_image, int restaurant_id, int food_price) {
        this.food_id = food_id;
        this.food_name = food_name;
        this.food_rating = food_rating;
        this.food_image = food_image;
        this.restaurant_id = restaurant_id;
        this.food_price = food_price;
    }
    public Food(){

    }

    public Boolean getAddedToFavorites() {
        return isAddedToFavorites;
    }

    public void setAddedToFavorites(Boolean addedToFavorites) {
        isAddedToFavorites = addedToFavorites;
    }

    public Boolean getAddedToCart() {
        return isAddedToCart;
    }

    public void setAddedToCart(Boolean addedToCart) {
        isAddedToCart = addedToCart;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public float getFood_rating() {
        return food_rating;
    }

    public void setFood_rating(float food_rating) {
        this.food_rating = food_rating;
    }

    public String getFood_image() {
        return food_image;
    }

    public void setFood_image(String food_image) {
        this.food_image = food_image;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public int getFood_price() {
        return food_price;
    }

    public void setFood_price(int food_price) {
        this.food_price = food_price;
    }
}
