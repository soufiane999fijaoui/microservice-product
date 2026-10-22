package com.microservice.product.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration principale : active la detection automatique des beans
 * annotes (DAO, Service) dans tout le microservice.
 */
@Configuration
@ComponentScan(basePackages = "com.microservice.product")
public class AppConfig {
}
