
package com.linda.food.models;


import java.io.Serializable;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Business implements Serializable
{
    private int restaurant_id;
    private String name;
    private float rating;
    private String location;
    private String image_url;
    private Boolean isclosed;
    private Double latitude;
    private Double longitude;

    public Business(int restaurant_id, String name, float rating, String location, String image_url, Boolean isclosed, Double latitude, Double longitude) {
        this.restaurant_id = restaurant_id;
        this.name = name;
        this.rating = rating;
        this.location = location;
        this.image_url = image_url;
        this.isclosed = isclosed;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Business(){

    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Boolean getIsclosed() {
        return isclosed;
    }

    public void setIsclosed(Boolean isclosed) {
        this.isclosed = isclosed;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
