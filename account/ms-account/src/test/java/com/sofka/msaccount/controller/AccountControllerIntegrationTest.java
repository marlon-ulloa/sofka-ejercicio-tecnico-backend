package com.sofka.msaccount.controller;

import com.sofka.msaccount.dto.AccountRequest;
import com.sofka.msaccount.entity.Cuenta;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private AccountRequest accountRequest;

    @Test
    public void createAccount() throws Exception{
        accountRequest = new AccountRequest();
        accountRequest.setNumeroCuenta("200008");
        accountRequest.setTipoCuenta("Ahorro");
        accountRequest.setClienteId("1");
        accountRequest.setSaldoInicial(BigDecimal.valueOf(1000));
        accountRequest.setEstado(true);
        accountRequest.setIdentificacion("0103080131");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(accountRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

}