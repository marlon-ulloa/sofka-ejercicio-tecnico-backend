package com.sofka.msclient.exception;

/**
 * Clase que captura la excepcion de recursos no encontrados
 */
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
