package com.example.ProductGrouping.Repository;

import com.example.ProductGrouping.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
