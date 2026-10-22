package com.microservice.product.dao;

import java.util.List;

import com.microservice.product.entity.Product;

import org.springframework.stereotype.Repository;

/**
 * Implementation du DAO produit : herite du comportement CRUD generique
 * et ajoute les requetes metier specifiques au catalogue.
 */
@Repository
public class ProductDaoImpl extends BaseDao<Product> implements ProductDao {

    @Override
    protected Class<Product> entityClass() {
        return Product.class;
    }

    @Override
    public List<Product> findByNameContaining(String keyword) {
        return execute(session -> session
                .createQuery("FROM Product WHERE LOWER(name) LIKE :keyword", Product.class)
                .setParameter("keyword", "%" + keyword.toLowerCase() + "%")
                .list());
    }

    @Override
    public long count() {
        return execute(session -> session
                .createQuery("SELECT COUNT(p) FROM Product p", Long.class)
                .getSingleResult());
    }
}
