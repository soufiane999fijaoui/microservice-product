package com.microservice.product.service;

import java.util.List;

import com.microservice.product.entity.Product;

/**
 * Couche de service : regle metier et orchestration transactionnelle.
 */
public interface ProductService {

    void addProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(Long id);

    Product getProduct(Long id);

    List<Product> getAllProducts();

    List<Product> searchProducts(String keyword);

    long count();
}
