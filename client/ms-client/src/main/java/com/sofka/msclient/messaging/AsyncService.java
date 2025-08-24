package com.sofka.msclient.messaging;

import com.sofka.msclient.entity.Client;
import com.sofka.msclient.exception.ResourceNotFoundException;
import com.sofka.msclient.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * CLase que se encuentra a la escucha de os mensajes en el topic de client-reques de kafka
 * para saber cuando enviar la informacion del cliente a kafka para el microservicio de cuentas
 */
@Service
public class AsyncService {
    @Autowired
    private ClientService clientService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Metodo que escucha los eventos del topic de kafka
     * @param clienteId
     * @throws Exception
     */
    @KafkaListener(topics = "client-request", groupId = "sofka")
    public void consumeClienteRequest(String clienteId) throws Exception {
        // Obtener datos del cliente desde la base de datos
        try {
            Client cliente = clientService.getClientByIdentification(clienteId);
            if (cliente == null) {
                throw new ResourceNotFoundException("Cliente no encontrado: " + clienteId);
            }

            if (!cliente.getEstado()){
                throw new ResourceNotFoundException("Cliente Inactivo: " + clienteId);
            }
            // Serializar el cliente a JSON
            String clienteJson = objectMapper.writeValueAsString(cliente);

            // Enviar respuesta a través de Kafka
            kafkaTemplate.send("client-response", clienteJson);
        }catch(ResourceNotFoundException ex){
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            error.put("identificacion", clienteId);
            String errorResponse = objectMapper.writeValueAsString(error);
            kafkaTemplate.send("client-response", errorResponse);
        }
    }
}
