package com.tnsif.onlineshopping.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.tnsif.onlineshopping.entities.Admin;
import com.tnsif.onlineshopping.entities.Customer;
import com.tnsif.onlineshopping.entities.Product;
import com.tnsif.onlineshopping.entities.Order;
import com.tnsif.onlineshopping.entities.ProductQuantityPair;

import com.tnsif.onlineshopping.services.AdminServices;
import com.tnsif.onlineshopping.services.CustomerServies;
import com.tnsif.onlineshopping.services.OrderServices;
import com.tnsif.onlineshopping.services.ProductServices;

public class OnlineShopping {

    // Service class instances
    private static ProductServices productService = new ProductServices();
    private static CustomerServies customerService = new CustomerServies();
    private static OrderServices orderService = new OrderServices();
    private static AdminServices adminService = new AdminServices();
    
    // Initial setup data matching the typical output flow
    private static final int INITIAL_ADMIN_ID = 1;
    private static final String INITIAL_ADMIN_USER = "admin";
    private static final String INITIAL_ADMIN_PASS = "pass123";

    public static void main(String[] args) {
        // Initialize one default admin for login
        adminService.addAdmin(new Admin(INITIAL_ADMIN_ID, INITIAL_ADMIN_USER, "admin@shop.com", INITIAL_ADMIN_PASS));
        
        // Initialize products to match the output's usage
        productService.addProduct(new Product(1001, "T-Shirt", 499.00, 50));
        productService.addProduct(new Product(1002, "Jeans", 1299.00, 30));
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n1. Admin Menu");
            System.out.println("2. Customer Menu");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            
            int choice;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                choice = -1;
                scanner.next(); 
            }
            scanner.nextLine();

            switch (choice) {
                case 1:
                    adminLoginAndModule(scanner);
                    break;
                case 2:
                    customerModule(scanner);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
    
    // --- ADMIN LOGIN AND MODULE ---
    private static void adminLoginAndModule(Scanner scanner) {
        System.out.print("Enter Admin Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();
        
        Admin authenticatedAdmin = adminService.getAdmins().stream()
                .filter(a -> a.getUsername().equals(username) && a.authenticate(password))
                .findFirst().orElse(null);
        
        if (authenticatedAdmin == null) {
            System.out.println("Invalid Admin credentials.");
            return;
        }

        int adminChoice;
        do {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add Product");
            System.out.println("2. Remove Product");
            System.out.println("3. View Products");
            System.out.println("4. Create Admin");
            System.out.println("5. View Admins");
            System.out.println("6. Update Order Status");
            System.out.println("7. View Orders");
            System.out.println("8. Return");
            System.out.print("Choose an option: ");
            
            if (scanner.hasNextInt()) {
                adminChoice = scanner.nextInt();
            } else {
                adminChoice = -1;
                scanner.next();
            }
            scanner.nextLine();

            switch (adminChoice) {
                case 1: addProduct(scanner); break;
                case 2: removeProduct(scanner); break;
                case 3: viewProducts(); break;
                case 4: createAdmin(scanner); break;
                case 5: viewAdmins(); break;
                case 6: updateOrderStatus(scanner); break;
                case 7: viewOrders(); break;
                case 8: System.out.println("Exiting Admin..."); break;
                default: System.out.println("Invalid choice! Please try again.");
            }
        } while (adminChoice != 8);
    }

    // --- CUSTOMER MODULE ---
    private static void customerModule(Scanner scanner) {
        int customerChoice;

        do {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Create Customer");
            System.out.println("2. View Customers");
            System.out.println("3. Place Order"); 
            System.out.println("4. View Orders"); 
            System.out.println("5. View Products"); 
            System.out.println("6. Return"); 
            System.out.print("Choose an option: "); 

            if (scanner.hasNextInt()) {
                customerChoice = scanner.nextInt();
            } else {
                customerChoice = -1;
                scanner.next();
            }
            scanner.nextLine();

            switch (customerChoice) {
                case 1: createCustomer(scanner); break;
                case 2: viewCustomers(); break;
                case 3: placeOrder(scanner); break;
                case 4: viewOrdersForCustomer(scanner); break;
                case 5: viewProducts(); break;
                case 6: System.out.println("Exiting Customer Menu..."); break;
                default: System.out.println("Invalid choice! Please try again.");
            }
        } while (customerChoice != 6);
    }
    
    // --- ADMIN HELPER METHODS ---
    private static void createAdmin(Scanner scanner) {
        System.out.print("Enter Admin ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Admin Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Admin Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();
        
        adminService.addAdmin(new Admin(userId, username, email, password));
        System.out.println("Admin created successfully! ID: " + userId);
    }
    
    private static void viewAdmins() {
        System.out.println("\n--- All Admins ---");
        adminService.getAdmins().forEach(System.out::println);
    }
    
    private static void addProduct(Scanner scanner) {
        System.out.print("Enter Product ID: ");
        int productId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Product Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter Stock Quantity: ");
        int stockQuantity = scanner.nextInt();
        scanner.nextLine();

        Product product = new Product(productId, name, price, stockQuantity);
        productService.addProduct(product);
        System.out.println("Product added successfully!");
    }

    private static void removeProduct(Scanner scanner) {
        System.out.print("Enter Product ID: ");
        int productId = scanner.nextInt();
        scanner.nextLine();
        
        productService.removeProduct(productId);
        System.out.println("Product removed successfully!");
    }
    
    private static void updateOrderStatus(Scanner scanner) {
        System.out.print("Enter Order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new status (Completed/Delivered/Cancelled): ");
        String status = scanner.nextLine();
        
        orderService.updateOrderStatus(orderId, status);
    }
    
    private static void viewOrders() { // Used by Admin
        System.out.println("\nOrders:");
        List<Order> orders = orderService.getOrders();
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        orders.forEach(System.out::println);
    }

    // --- CUSTOMER HELPER METHODS ---
    private static void createCustomer(Scanner scanner) {
        System.out.print("Enter User ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter Password (for future login): ");
        String password = scanner.nextLine();
        
        Customer newCustomer = new Customer(userId, username, email, address, password);
        customerService.addCustomer(newCustomer);
        System.out.println("Customer created successfully! ID: " + userId);
    }
    
    private static void viewCustomers() {
        System.out.println("\nCustomers:");
        customerService.getCustomers().forEach(System.out::println);
    }
    
    private static void viewOrdersForCustomer(Scanner scanner) {
        System.out.print("Enter Customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();
        
        Customer customer = customerService.getCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        
        System.out.println("\nOrders:");
        List<Order> customerOrders = orderService.getOrders().stream()
            .filter(order -> order.getCustomer().getUserId() == customerId)
            .toList();

        if (customerOrders.isEmpty()) {
            System.out.println("No orders found for this customer.");
            return;
        }
            
        customerOrders.forEach(order -> {
            System.out.println("Order ID: " + order.getOrderId() + ", Status: " + order.getStatus());
            for (ProductQuantityPair pair : order.getProducts()) {
                System.out.println("  Product: " + pair.getProduct().getName() + ", Quantity: " + pair.getQuantity());
            }
        });
    }

    private static void placeOrder(Scanner scanner) {
        System.out.print("Enter Customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();

        Customer customer = customerService.getCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer not found. Please create a customer first.");
            return;
        }

        List<ProductQuantityPair> cart = new ArrayList<>();
        int productId, quantity;

        do {
            System.out.print("Enter Product ID to add to order (or -1 to complete): ");
            if (scanner.hasNextInt()) {
                productId = scanner.nextInt();
            } else {
                System.out.println("Invalid input.");
                scanner.next(); 
                continue;
            }

            if (productId == -1) break;

            System.out.print("Enter quantity: ");
            if (scanner.hasNextInt()) {
                quantity = scanner.nextInt();
            } else {
                System.out.println("Invalid quantity input.");
                scanner.next();
                continue;
            }
            scanner.nextLine();

            Product product = productService.getProductById(productId);
            if (product == null) {
                System.out.println("Product not found. Try again.");
                continue;
            }

            if (quantity > 0) {
                cart.add(new ProductQuantityPair(product, quantity));
            } else {
                System.out.println("Quantity must be greater than zero.");
            }

        } while (true);

        if (!cart.isEmpty()) {
            Order newOrder = new Order(customer, cart);
            orderService.placeOrder(newOrder);
            customer.addOrder(newOrder);
            System.out.println("Order placed successfully!");
        } else {
            System.out.println("Order cancelled or cart is empty.");
        }
    }

    private static void viewProducts() { // Used by Customer/Admin
        System.out.println("\nProducts:");
        if (productService.getProducts().isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        productService.getProducts().forEach(System.out::println);
    }
}