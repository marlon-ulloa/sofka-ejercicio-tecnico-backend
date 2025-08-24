package com.sofka.msaccount.messaging;

import com.sofka.msaccount.dto.AccountDTO;
import com.sofka.msaccount.exception.ResourceNotFoundException;
import com.sofka.msaccount.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Metodo que escucha si el microservicio de cliente envia el mensaje de desactivar las cuentas
 */
@Service
public class AsyncService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "account-deactive-request", groupId = "sofka")
    public void consumeClienteRequest(String clienteId) throws Exception {
        // Obtener datos del cliente desde la base de datos
        try {
            List<AccountDTO> accounts = accountService.findByClientId(Long.parseLong(clienteId));
            if (accounts.isEmpty()) {
                Map<String, Object> success = new HashMap<>();
                success.put("success", "Sin Cuentas para desactivar");
                success.put("clienteId", clienteId);
                String successResponse = objectMapper.writeValueAsString(success);
                // Enviar respuesta a través de Kafka
                kafkaTemplate.send("account-deactive-response", successResponse);
            }

            //desactiva las cuentas con un loop
            for (AccountDTO account: accounts){
                account.setEstado(false);
                accountService.updateCuenta((long) account.getId(), account);
            }
            Map<String, Object> success = new HashMap<>();
            success.put("success", "Cuentas desactivadas");
            success.put("clienteId", clienteId);
            String successResponse = objectMapper.writeValueAsString(success);
            // Enviar respuesta a través de Kafka
            kafkaTemplate.send("account-deactive-response", successResponse);
        }catch(ResourceNotFoundException ex){
            Map<String, Object> success = new HashMap<>();
            success.put("success", "Sin Cuentas para desactivar");
            success.put("clienteId", clienteId);
            String successResponse = objectMapper.writeValueAsString(success);
            // Enviar respuesta a través de Kafka
            kafkaTemplate.send("account-deactive-response", successResponse);
        }
    }

}
