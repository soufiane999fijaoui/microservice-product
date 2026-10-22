package com.microservice.product.exception;

/**
 * Leve lorsqu'une ressource demandee n'existe pas en base.
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
