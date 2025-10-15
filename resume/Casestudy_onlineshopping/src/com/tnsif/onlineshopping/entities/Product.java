package com.tnsif.onlineshopping.entities;

public class Product {
    private int productId;
    private String name;
    private double price;
    private int stockQuantity;

    public Product(int productId, String name, double price, int stockQuantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public String toString1() {
        return String.format("ID: %d, Name: %s, Price: %.2f, Stock: %d", productId, name, price, stockQuantity);
    }


    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Price: $%.2f, Stock: %d", productId, name, price, stockQuantity);
    }
}