package com.sofka.msaccount.util;

import com.sofka.msaccount.exception.ResourceNotFoundException;
import com.sofka.msaccount.messaging.AsyncConsumerService;
import com.sofka.msaccount.messaging.AsyncProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Clase que se encarga de realizar las validaciones necesarias
 */
@Component
public class Validation {


    @Autowired
    private AsyncProducerService asyncProducerService;
    @Autowired
    private AsyncConsumerService asyncConsumerService;
    public Validation(){}

    public Long validateClientExists(String identification){
        asyncProducerService.sendClienteRequest(identification);
        Map<String, Object> client = asyncConsumerService.obtenerRespuesta(identification);
        if (client.containsKey("error")) {
            throw new ResourceNotFoundException(client.get("error").toString());
        }

        String clientId= client.get("id").toString();
        if (clientId == null || clientId.isEmpty()) {
            throw new IllegalArgumentException("El cliente no existe o se encuentra inactivo");
        }
        return Long.parseLong(clientId);
    }
}
