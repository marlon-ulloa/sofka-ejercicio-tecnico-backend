package com.sofka.msclient.exception;

/**
 * CLase para el manejo de las excepciones de intento de ingreso de clientes existentes
 */
public class DuplicateClientException extends RuntimeException{
    public DuplicateClientException(String message) {
        super(message);
    }
}
