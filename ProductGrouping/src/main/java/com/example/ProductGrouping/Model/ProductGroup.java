package com.example.ProductGrouping.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_groups")
public class ProductGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double totalPrice; // Total price of products in this group

    @OneToMany(mappedBy = "productGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>(); // Инициализация коллекции

    // Конструктор
    public ProductGroup() {
        this.products = new ArrayList<>(); // Дополнительная инициализация
    }

    // Parameterized constructor
    public ProductGroup(double totalPrice) {
        this.totalPrice = totalPrice;
    }


    // Getter for id
    public Long getId() {
        return id;
    }

    // Setter for id
    public void setId(Long id) {
        this.id = id;
    }

    // Getter for totalPrice
    public double getTotalPrice() {
        return totalPrice;
    }

    // Setter for totalPrice
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Getter for products
    public List<Product> getProducts() {
        return products;
    }

    // Setter for products
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    // Method to add a product to the group
    public void addProduct(Product product) {
        products.add(product);
        product.setGroup(this); // Set the group for the product
    }

    // Method to calculate the total price of products in the group
    public void calculateTotalPrice() {
        totalPrice = products.stream().mapToDouble(Product::getPrice).sum();
    }
}
