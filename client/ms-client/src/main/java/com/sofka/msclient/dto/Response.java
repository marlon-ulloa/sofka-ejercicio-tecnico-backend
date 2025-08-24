package com.sofka.msclient.dto;

/**
 * Clase DTO para manejo de respuestas
 */
public class Response {
    private int code;
    private String message;
    private ClientDTO clientDTO;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ClientDTO getClientDTO() {
        return clientDTO;
    }

    public void setClientDTO(ClientDTO clientDTO) {
        this.clientDTO = clientDTO;
    }
}
