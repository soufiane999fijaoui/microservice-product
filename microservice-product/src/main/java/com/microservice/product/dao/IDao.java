package com.microservice.product.dao;

import java.util.List;

/**
 * Contrat generique du pattern DAO : operations CRUD reutilisables
 * pour toutes les entites du microservice.
 *
 * @param <T> type de l'entite manipulee
 */
public interface IDao<T> {

    void save(T entity);

    void update(T entity);

    void delete(T entity);

    T findById(Long id);

    List<T> findAll();
}
