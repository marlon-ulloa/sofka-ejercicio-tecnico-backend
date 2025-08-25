package com.sofka.msclient.controller;

import com.sofka.msclient.entity.Client;
import com.sofka.msclient.repository.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClientRepository clientRepository;

    private Client client;

    @BeforeEach
    public void setup(){

    }

    @Test
    public void createClient() throws Exception{
        client = new Client();
        client.setIdentificacion("123456780");
        client.setNombre("Prueba Integracion");
        client.setGenero("Masculino");
        client.setEdad(50);
        client.setTelefono("2258971");
        client.setContrasena("passowrd");
        client.setDireccion("Direccion de prueba");
        client.setEstado(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(client);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void getAllClients() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/clientes"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}