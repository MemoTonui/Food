package com.linda.food.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class User implements Serializable
{

    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("firebaseUid")
    @Expose
    private String firebaseUid;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("orders")
    @Expose
    private List<Object> orders = null;
    @SerializedName("__v")
    @Expose
    private Integer v;
    private final static long serialVersionUID = 4711264992423541269L;

    /**
     * No args constructor for use in serialization
     *
     */
    public User() {
    }

    public User(String location, String imgUrl, String id, String username, String fullName, String phoneNumber, String firebaseUid, String email, List<Object> orders, Integer v) {
        this.location = location;
        this.imgUrl = imgUrl;
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.firebaseUid = firebaseUid;
        this.email = email;
        this.orders = orders;
        this.v = v;
    }

    /**
     *
     * @param imgUrl
     * @param phoneNumber
     * @param v
     * @param name
     * @param location
     * @param orders
     * @param id
     * @param email
     */



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Object> getOrders() {
        return orders;
    }

    public void setOrders(List<Object> orders) {
        this.orders = orders;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }
}