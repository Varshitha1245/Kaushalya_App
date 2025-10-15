package com.tnsif.onlineshopping.services;

import java.util.ArrayList;
import java.util.List;
import com.tnsif.onlineshopping.entities.Customer;

public class CustomerServies {
    private List<Customer> customerList = new ArrayList<>();

    public void addCustomer(Customer customer) {
        customerList.add(customer);
    }

    public Customer getCustomer(int userId) {
        return customerList.stream()
                .filter(customer -> customer.getUserId() == userId)
                .findFirst()
                .orElse(null);
    }

    public List<Customer> getCustomers() {
        return customerList;
    }
}