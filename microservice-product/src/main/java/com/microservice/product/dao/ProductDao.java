package com.microservice.product.dao;

import java.util.List;

import com.microservice.product.entity.Product;

/**
 * DAO specialise du produit : herite du contrat generique {@link IDao}
 * et ajoute les requetes metier du catalogue.
 */
public interface ProductDao extends IDao<Product> {

    List<Product> findByNameContaining(String keyword);

    long count();
}
