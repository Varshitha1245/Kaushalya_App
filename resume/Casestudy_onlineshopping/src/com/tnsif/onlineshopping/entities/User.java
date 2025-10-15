package com.tnsif.onlineshopping.entities;

// Base class for Admin and Customer
public abstract class User {
    private int userId;
    private String username;
    private String email;
    private String password;

    public User(int userId, String username, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    
    // Authentication method
    public boolean authenticate(String inputPassword) {
        return this.password.equals(inputPassword);
    }
    
    @Override
    public String toString() {
        return "User ID: " + userId + ", Username: " + username + ", Email: " + email;
    }
}