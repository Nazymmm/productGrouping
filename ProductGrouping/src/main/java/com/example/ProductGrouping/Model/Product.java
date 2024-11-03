package com.example.ProductGrouping.Model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Name of the product
    private double price; // Price of the product
    private String unit; // Unit of measurement (e.g., kg, liters)

    @Column(nullable = false)
    private double pricePerUnit = 0.0f;

    @Column(nullable = false)
    private Integer quantity = 0;

    @ManyToOne(fetch = FetchType.LAZY) // Defines a many-to-one relationship with ProductGroup
    @JoinColumn(name = "group_id") // Foreign key column in the Product table
    private ProductGroup group; // Reference to the ProductGroup this product belongs to

    // Default constructor
    public Product() {
    }

    // Parameterized constructor
    public Product(String name, double price, String unit, double pricePerUnit, int quantity, ProductGroup group) {
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.group = group;
    }

    // Getter and setter for id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for price
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Getter and setter for unit
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    // Getter and setter for pricePerUnit
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    // Getter and setter for quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getter and setter for group
    public ProductGroup getGroup() {
        return group;
    }

    public void setGroup(ProductGroup group) {
        this.group = group;
    }
}
