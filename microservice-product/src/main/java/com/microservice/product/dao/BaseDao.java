package com.microservice.product.dao;

import java.util.List;
import java.util.function.Function;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Implementation generique du DAO : factorise les operations CRUD pour
 * toutes les entites du microservice.
 *
 * @param <T> type de l'entite manipulee
 */
public abstract class BaseDao<T> implements IDao<T> {

    @Autowired
    protected SessionFactory sessionFactory;

    /** Type Hibernate concret de l'entite traitee. */
    protected abstract Class<T> entityClass();

    /** Nom de l'entite utilise dans les requetes HQL. */
    protected String entityName() {
        return entityClass().getSimpleName();
    }

    /** Execute un traitement sur la session Hibernate liee a la transaction. */
    protected <R> R execute(Function<Session, R> callback) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            return callback.apply(sessionFactory.getCurrentSession());
        }
        try (Session session = sessionFactory.openSession()) {
            return callback.apply(session);
        }
    }

    @Override
    public void save(T entity) {
        execute(session -> {
            session.save(entity);
            return null;
        });
    }

    @Override
    public void update(T entity) {
        execute(session -> {
            if (session.contains(entity)) {
                session.merge(entity);
            } else {
                session.update(entity);
            }
            return null;
        });
    }

    @Override
    public void delete(T entity) {
        execute(session -> {
            session.delete(session.contains(entity) ? entity : session.merge(entity));
            return null;
        });
    }

    @Override
    public T findById(Long id) {
        return execute(session -> session.get(entityClass(), id));
    }

    @Override
    public List<T> findAll() {
        return execute(session -> session.createQuery("FROM " + entityName(), entityClass()).list());
    }

    /** Force l'envoi des instructions SQL en attente. */
    public void flush() {
        execute(session -> {
            session.flush();
            return null;
        });
    }
}
