package com.sofka.msclient.util;

import com.sofka.msclient.dto.Response;

public class FormatResponse {
    private static FormatResponse formatResponse;
    public FormatResponse(){}

    public static FormatResponse getInstance() {
        if (formatResponse == null) { // Primera verificación sin bloqueo
            synchronized (FormatResponse.class) { // Bloqueo para hilos concurrentes
                if (formatResponse == null) { // Segunda verificación
                    formatResponse = new FormatResponse();
                }
            }
        }
        return formatResponse;
    }

    public Response formatResponse(int code, String message){
        Response response = new Response();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}
