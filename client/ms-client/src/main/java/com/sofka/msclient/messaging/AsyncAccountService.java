package com.sofka.msclient.messaging;

import com.sofka.msclient.entity.Client;
import com.sofka.msclient.exception.MessageNotSentException;
import com.sofka.msclient.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Clase que envia un evento a kafka para que el microservicio de cuentas desactive las cuentas del cliente a ser eliminado
 */
@Service
public class AsyncAccountService {

    private final Map<String, Map<String, Object>> responses = new ConcurrentHashMap<>();
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AsyncAccountService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Metodo que envia el evento a kafka con el id del cliente
     * @param clientId Indica el id del cliente
     */
    public void sendAccountDeleteRequest(Long clientId){
        kafkaTemplate.send("account-deactive-request", clientId.toString()).whenComplete((result, ex) -> {
            if (ex != null) {
                System.out.println("Error al enviar mensaje: " + ex.getMessage());
                throw new MessageNotSentException("Error al enviar mensaje: " + ex.getMessage());
            } else {
                System.out.println("Mensaje enviado correctamente, metadata: " + result.getRecordMetadata());
            }
        });

    }

    /**
     * Metodo que escucha la respuesta del microservicio de cuentas
     * @param accountJson Indica el mensaje que obtiene de la cola de kafka del topic de respuesta de desactivacion de cuenta
     * @throws Exception
     */
    @KafkaListener(topics = "account-deactive-response", groupId = "sofka")
    public void consumeAccountRequest(String accountJson) throws Exception {
        Map<String, Object> account = objectMapper.readValue(accountJson, Map.class);
        responses.put(account.get("clienteId").toString(), account);
    }

    /**
     * Metodo que lee la respuesta de lo que obtuvo de kafka y retorna el resultado para continuar con el proceso de eliminacion
     * @param clienteId Indica el id del cliente a verficar si coincide con el resultado del microservicio de cuentas
     * @return retorna la respuesta del microservicio de cuentas
     */
    public Map<String, Object> obtainResponse(Long clienteId) {
        // Esperar hasta que la respuesta esté disponible (con timeout)
        long timeout = 5000; // 5 segundos
        long startTime = System.currentTimeMillis();
        while (!responses.containsKey(clienteId.toString())) {
            if (System.currentTimeMillis() - startTime > timeout) {
                throw new RuntimeException("Timeout esperando respuesta del cliente: " + clienteId);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupción mientras se esperaba respuesta", e);
            }
        }
        // Retornar y eliminar la respuesta para evitar duplicados
        return responses.remove(clienteId.toString());
    }
}
