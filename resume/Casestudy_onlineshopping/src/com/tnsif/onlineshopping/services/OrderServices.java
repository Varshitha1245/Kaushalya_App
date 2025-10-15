package com.tnsif.onlineshopping.services;

import java.util.ArrayList;
import java.util.List;
import com.tnsif.onlineshopping.entities.Order;
import com.tnsif.onlineshopping.entities.Product;
import com.tnsif.onlineshopping.entities.ProductQuantityPair;

public class OrderServices {
    private List<Order> orderList = new ArrayList<>();

    public void placeOrder(Order order) {
        orderList.add(order);
    }

    public void updateOrderStatus(int orderId, String status) {
        Order order = getOrderById(orderId);

        if (order != null) {
            
            // Logic for status transition to COMPLETED (Deduct Stock)
            if (status.equalsIgnoreCase("Completed") && order.getStatus().equalsIgnoreCase("Pending")) {
                for (ProductQuantityPair pair : order.getProducts()) { 
                    Product product = pair.getProduct();
                    int quantity = pair.getQuantity();

                    if (product.getStockQuantity() >= quantity) {
                        product.setStockQuantity(product.getStockQuantity() - quantity);
                    } else {
                        System.out.println("Insufficient stock for product:" + product.getName());
                        return; // Abort update if stock is insufficient
                    }
                }
            
            // Logic for status transition to CANCELLED (Restore Stock)
            } else if (status.equalsIgnoreCase("Cancelled")) {
                if (order.getStatus().equalsIgnoreCase("Completed") || order.getStatus().equalsIgnoreCase("Pending")) {
                    // Restore stock only if moving from Pending or Completed to Cancelled
                    for (ProductQuantityPair pair : order.getProducts()) {
                        Product product = pair.getProduct();
                        int quantity = pair.getQuantity();
                        product.setStockQuantity(product.getStockQuantity() + quantity);
                    }
                }
            
            // Logic for status transition to DELIVERED (No Stock Change)
            } else if (status.equalsIgnoreCase("Delivered") && order.getStatus().equalsIgnoreCase("Completed")) {
                // Status update only
            } else {
                System.out.println("Invalid Order Status. Cannot transition from " + order.getStatus() + " to " + status);
                System.out.println("Invalid Order"); 
                return;
            }
            order.setStatus(status);
            System.out.println("Order status updated to " + status + ".");
        } else {
            System.out.println("Invalid Order Id.");
        }
    }

    public Order getOrderById(int orderId) {
        return orderList.stream().filter(order -> order.getOrderId() == orderId).findFirst().orElse(null);
    }

    public List<Order> getOrders() {
        return orderList;
    }
}