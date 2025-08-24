package com.sofka.msaccount.messaging;

import com.sofka.msaccount.exception.MessageNotSentException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Clase que se encarga de enviar a kafka el mensaje de solicitud de informacion del cliente
 */
@Service
public class AsyncProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public AsyncProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Metodo que envia el mensaje de solicitud de informacion al cliente a la cola de kafka
     * @param clienteId Indica el id del cliente a enviar
     */
    public void sendClienteRequest(String clienteId) {
        kafkaTemplate.send("client-request", clienteId).whenComplete((result, ex) -> {
            if (ex != null) {
                System.out.println("Error al enviar mensaje: " + ex.getMessage());
                throw new MessageNotSentException("Error al enviar mensaje: " + ex.getMessage());
            } else {
                System.out.println("Mensaje enviado correctamente, metadata: " + result.getRecordMetadata());
            }
        });
    }
}
