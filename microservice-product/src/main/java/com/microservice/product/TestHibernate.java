package com.microservice.product;

import com.microservice.product.config.AppConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Verification du demarrage du conteneur Spring : initialisation de la
 * SessionFactory et de la connexion a la base de donnees.
 */
public class TestHibernate {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestHibernate.class);

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            System.out.println("Contexte Spring initialise avec succes !");
            LOGGER.info("SessionFactory : {}", context.getBean(org.hibernate.SessionFactory.class).getClass().getName());
        } catch (Exception exception) {
            LOGGER.error("Echec de l'initialisation : {}", exception.getMessage());
            LOGGER.error("Verifiez jdbc.url / jdbc.username / jdbc.password dans application.properties");
        }
    }
}
