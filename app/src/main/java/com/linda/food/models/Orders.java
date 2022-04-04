package com.linda.food.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Orders implements Serializable {
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("date")
    @Expose
    private Date date;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("subTotal")
    @Expose
    private int subTotal;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("deliveryCost")
    @Expose
    private int deliveryCost;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("food")
    @Expose
    private List<Food> food = null;
    @SerializedName("__v")
    @Expose
    private Integer v;


    public Orders() {
    }

    public Orders(String location, int subTotal, String id, int deliveryCost, String user, List<Food> food, Integer v) {
        this.location = location;
        this.subTotal = subTotal;
        this.id = id;
        this.deliveryCost = deliveryCost;
        this.user = user;
        this.food = food;
        this.v = v;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(int deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Food> getFood() {
        return food;
    }

    public void setFood(List<Food> food) {
        this.food = food;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
