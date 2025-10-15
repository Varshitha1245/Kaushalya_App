package com.tnsif.onlineshopping.entities;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private String address;
    private List<Order> orders;

    public Customer(int userId, String username, String email, String address, String password) {
        super(userId, username, email, password);
        this.address = address;
        this.orders = new ArrayList<>();
    }

    // Getters
    public String getAddress() { return address; }
    public List<Order> getOrders() { return orders; }

    public void addOrder(Order order) {
        orders.add(order);
    }
    
    @Override
    public String toString() {
        return super.toString() + ", Address: " + address;
    }
}