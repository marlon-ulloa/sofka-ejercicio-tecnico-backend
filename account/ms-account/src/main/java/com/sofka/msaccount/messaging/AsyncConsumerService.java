package com.sofka.msaccount.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * CLase que escucha los mensajes en la cola del topico de kafka que corresponden a la respuesta del cliente sobre la informacion que se solicta para crear una cuenta
 */
@Service
public class AsyncConsumerService {
    private final Map<String, Map<String, Object>> respuestas = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Metodo que escucha para obtener una respuesta del microservicio del cliente para saber si existe o no
     * @param clienteJson Indica el mensaje que obtiene de kafka
     * @throws Exception
     */
    @KafkaListener(topics = "client-response", groupId = "sofka")
    public void consumeClienteResponse(String clienteJson) throws Exception {
        // Deserializar el JSON a Cliente
        Map<String, Object> client = objectMapper.readValue(clienteJson, Map.class);

        respuestas.put(client.get("identificacion").toString(), client);
    }

    /**
     * Metodo que gestiona la informadion obtenida de la cola del topico de kafka de la respuesta del microservicio cliente y la valida
     * @param identification Indica la identificacion a validar
     * @return Retorna la data validada
     */
    public Map<String, Object> obtenerRespuesta(String identification){
        // Esperar hasta que la respuesta esté disponible (con timeout)
        long timeout = 5000; // 5 segundos
        long startTime = System.currentTimeMillis();

        while (!respuestas.containsKey(identification)) {
            if (System.currentTimeMillis() - startTime > timeout) {
                try {
                    throw new TimeoutException("Timeout esperando respuesta del cliente: " + identification);
                }catch(TimeoutException ex){
                    System.out.println(ex.getMessage());
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupción mientras se esperaba respuesta", e);
            }
        }

        // Retornar y eliminar la respuesta para evitar duplicados
        return respuestas.remove(identification);
    }
}
