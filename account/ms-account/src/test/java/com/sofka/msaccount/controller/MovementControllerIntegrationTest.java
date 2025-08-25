package com.sofka.msaccount.controller;

import com.sofka.msaccount.dto.AccountDTO;
import com.sofka.msaccount.entity.Cuenta;
import com.sofka.msaccount.entity.Movimiento;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
class MovementControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    private Movimiento movement;
    private AccountDTO accountDTO;

    //@Test
    public void createAccount() throws Exception{
        Cuenta account = new Cuenta();
        account.setNumeroCuenta("400001");
        movement = new Movimiento();
        movement.setTipoMovimiento("Retiro");
        movement.setCuenta(account);
        movement.setValor(BigDecimal.valueOf(100));
        accountDTO = new AccountDTO();
        accountDTO.setNumeroCuenta("200000");
        accountDTO.setTipoCuenta("Corriente");
        accountDTO.setSaldoInicial(BigDecimal.valueOf(1000));
        accountDTO.setEstado(true);
        accountDTO.setIdentificacion("123456745");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(movement);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

}