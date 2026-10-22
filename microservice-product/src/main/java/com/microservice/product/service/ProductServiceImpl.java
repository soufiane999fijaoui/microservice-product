package com.microservice.product.service;

import java.util.List;

import com.microservice.product.dao.ProductDao;
import com.microservice.product.entity.Product;
import com.microservice.product.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation du service produit : isole la logique metier et s'appuie
 * sur le DAO generique pour l'acces aux donnees.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Transactional
    @Override
    public void addProduct(Product product) {
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Le prix ne peut pas etre negatif");
        }
        productDao.save(product);
    }

    @Transactional
    @Override
    public void updateProduct(Product product) {
        Product existing = productDao.findById(product.getId());
        if (existing == null) {
            throw new ResourceNotFoundException("Produit introuvable : id=" + product.getId());
        }
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        productDao.update(existing);
    }

    @Transactional
    @Override
    public void deleteProduct(Long id) {
        Product existing = productDao.findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Produit introuvable : id=" + id);
        }
        productDao.delete(existing);
    }

    @Transactional(readOnly = true)
    @Override
    public Product getProduct(Long id) {
        Product product = productDao.findById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Produit introuvable : id=" + id);
        }
        return product;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Product> searchProducts(String keyword) {
        return productDao.findByNameContaining(keyword);
    }

    @Transactional(readOnly = true)
    @Override
    public long count() {
        return productDao.count();
    }
}
