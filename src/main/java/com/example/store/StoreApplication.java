package com.example.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*") // Allows your HTML file to communicate with this server
public class StoreApplication {

    // 1. The Product Model (Includes Name, Image, Price, Category, Description)
    public static class Product {
        public String id;
        public String name;
        public String imageUrl;
        public double price;
        public String category;
        public String description;

        public Product() {} // Empty constructor required for JSON parsing

        public Product(String id, String name, String imageUrl, double price, String category, String description) {
            this.id = id;
            this.name = name;
            this.imageUrl = imageUrl;
            this.price = price;
            this.category = category;
            this.description = description;
        }
    }

    // In-memory Database
    private final Map<String, Product> database = new ConcurrentHashMap<>();

    public StoreApplication() {
        // Sample item so the dashboard isn't completely empty when you start
        database.put("P100", new Product("P100", "Admin Sample Item", "https://via.placeholder.com/150", 19.99, "Accessories", "This is a sample description."));
    }

    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }

    // ==========================================
    // CRUD ENDPOINTS
    // ==========================================

    // READ: Get all items
    @GetMapping("/products")
    public Collection<Product> getAllProducts() {
        return database.values();
    }

    // CREATE: Add a new item
    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) {
        String newId = "P-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        product.id = newId;
        database.put(newId, product);
        return product;
    }

    // UPDATE: Modify an existing item
    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable String id, @RequestBody Product updatedProduct) {
        if (database.containsKey(id)) {
            updatedProduct.id = id; // Ensure the ID doesn't change
            database.put(id, updatedProduct);
            return updatedProduct;
        }
        throw new RuntimeException("Product not found");
    }

    // DELETE: Remove an item
    @DeleteMapping("/products/{id}")
    public Map<String, Boolean> deleteProduct(@PathVariable String id) {
        database.remove(id);
        return Map.of("success", true);
    }
}