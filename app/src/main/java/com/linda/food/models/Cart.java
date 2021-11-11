package com.linda.food.models;

public class Cart {

    private String food_name;
    private int food_price;
    private int quantity;

    public Cart(String food_name, int food_price, int quantity) {
        this.food_name = food_name;
        this.food_price = food_price;
        this.quantity = quantity;
    }

    public Cart() {

    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public int getFood_price() {
        return food_price;
    }

    public void setFood_price(int food_price) {
        this.food_price = food_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
