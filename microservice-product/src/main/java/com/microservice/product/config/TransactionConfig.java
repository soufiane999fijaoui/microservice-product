package com.microservice.product.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Gestion declarative des transactions : l'annotation {@code @Transactional}
 * suffit a ouvrir/valider/annuler une transaction autour d'une methode.
 */
@Configuration
@EnableTransactionManagement
public class TransactionConfig {

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }
}
