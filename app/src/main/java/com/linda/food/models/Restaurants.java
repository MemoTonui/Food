package com.linda.food.models;

public class Restaurants {
    private String restaurant_name;
    private int restaurant_ratings;
    private String restaurant_location;
    private String restaurant_image;
    private String restaurant_longitude;
    private String restaurant_latitude;

    public Restaurants(String restaurant_name, int restaurant_ratings, String restaurant_location, String restaurant_image, String restaurant_longitude, String restaurant_latitude) {
        this.restaurant_name = restaurant_name;
        this.restaurant_ratings = restaurant_ratings;
        this.restaurant_location = restaurant_location;
        this.restaurant_image = restaurant_image;
        this.restaurant_longitude = restaurant_longitude;
        this.restaurant_latitude = restaurant_latitude;
    }

    public Restaurants(){

    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public int getRestaurant_ratings() {
        return restaurant_ratings;
    }

    public void setRestaurant_ratings(int restaurant_ratings) {
        this.restaurant_ratings = restaurant_ratings;
    }

    public String getRestaurant_location() {
        return restaurant_location;
    }

    public void setRestaurant_location(String restaurant_location) {
        this.restaurant_location = restaurant_location;
    }

    public String getRestaurant_image() {
        return restaurant_image;
    }

    public void setRestaurant_image(String restaurant_image) {
        this.restaurant_image = restaurant_image;
    }

    public String getRestaurant_longitude() {
        return restaurant_longitude;
    }

    public void setRestaurant_longitude(String restaurant_longitude) {
        this.restaurant_longitude = restaurant_longitude;
    }

    public String getRestaurant_latitude() {
        return restaurant_latitude;
    }

    public void setRestaurant_latitude(String restaurant_latitude) {
        this.restaurant_latitude = restaurant_latitude;
    }
}
