package com.tnsif.onlineshopping.entities;

import java.util.List;

public class Order {
    private static int orderCount = 0; 
    
    private int orderId;
    private Customer customer;
    private List<ProductQuantityPair> products;
    private String status;

    public Order(Customer customer, List<ProductQuantityPair> products) {
        this.orderId = ++orderCount; 
        this.customer = customer;
        this.products = products;
        this.status = "Pending"; // Default initial status
    }

    // Getters
    public int getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }
    public List<ProductQuantityPair> getProducts() { return products; }
    public String getStatus() { return status; }

    // Setter
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(orderId)
          .append(", Customer: ").append(customer.getUsername())
          .append(", Status: ").append(status);

        for (ProductQuantityPair pair : products) {
            sb.append("\n  Product: ").append(pair.getProduct().getName())
              .append(", Quantity: ").append(pair.getQuantity());
        }
        return sb.toString();
    }
}