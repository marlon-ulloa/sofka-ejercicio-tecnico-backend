package com.sofka.msclient.util;

import com.sofka.msclient.exception.DuplicateClientException;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class Validation {

    /**
     * Metodo que realiza una validacion del tipo de excepcion que se ha generado
     * @param ex Indica la excepcion que se dio en el runtime
     */
    public void validateException(DataIntegrityViolationException ex, String identification){
        if (ex.getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) ex.getCause();
            // Verifica si la causa de la ConstraintViolationException es una PSQLException
            if (cve.getCause() instanceof org.postgresql.util.PSQLException) {
                org.postgresql.util.PSQLException psqlEx = (org.postgresql.util.PSQLException) cve.getCause();
                // Obtiene el código de estado SQL
                String sqlState = psqlEx.getSQLState();
                // Comprueba si es el código para violación de clave única (23505)
                if (PSQLState.UNIQUE_VIOLATION.getState().equals(sqlState)) {
                    throw new DuplicateClientException("Cliente con identificación " + identification + " ya existe.");
                }
            }
        }
    }
}
