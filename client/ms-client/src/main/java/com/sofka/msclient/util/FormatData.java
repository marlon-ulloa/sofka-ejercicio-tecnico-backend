package com.sofka.msclient.util;

import com.sofka.msclient.dto.ClientDTO;
import com.sofka.msclient.dto.Response;
import com.sofka.msclient.entity.Client;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class FormatData {
    private static FormatData formatData;
    public FormatData(){}

    public static FormatData getInstance() {
        if (formatData == null) { // Primera verificación sin bloqueo
            synchronized (FormatData.class) { // Bloqueo para hilos concurrentes
                if (formatData == null) { // Segunda verificación
                    formatData = new FormatData();
                }
            }
        }
        return formatData;
    }
    public Client clientDTOtoClient(ClientDTO clientDTO){
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(clientDTO, Client.class);
    }
    public ClientDTO clientToClientDTO(Client client){
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(client, ClientDTO.class);
    }

    public List<ClientDTO> formatClientList(List<Client> clients){
        List<ClientDTO> clientsDTOList = new ArrayList<>();
        for(Client client: clients){
            clientsDTOList.add(clientToClientDTO(client));
        }
        return clientsDTOList;

    }

    public Client formatUpdateClient(Client client, ClientDTO clientDetails){
        client.setNombre(clientDetails.getNombre());
        client.setGenero(clientDetails.getGenero());
        client.setEdad(clientDetails.getEdad());
        client.setIdentificacion(clientDetails.getIdentificacion());
        client.setDireccion(clientDetails.getDireccion());
        client.setTelefono(clientDetails.getTelefono());
        client.setContrasena(clientDetails.getContrasena());
        client.setEstado(clientDetails.getEstado());
        return client;
    }

    public Response formatResponse(int code, String message){
        Response response = new Response();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}
