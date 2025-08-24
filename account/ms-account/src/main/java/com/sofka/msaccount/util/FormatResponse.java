package com.sofka.msaccount.util;

import com.sofka.msaccount.dto.GeneralResponse;
import com.sofka.msaccount.dto.Response;
import com.sofka.msaccount.entity.Movimiento;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que se encarga de mappear la respuesta a ser enviada al endpoint
 */
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

    public Response formatResponse(int code, String message, Object data){
        Response response = new Response();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public List<GeneralResponse> formatResponse(List<Movimiento> movements, String clientName){
        List<GeneralResponse> movementsList = new ArrayList<>();
        for(Movimiento movement: movements){
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setFecha(movement.getFecha().format(DateTimeFormatter.ofPattern("dd/M/yyyy")));
            generalResponse.setCliente(clientName);
            generalResponse.setNumeroCuenta(movement.getCuenta().getNumeroCuenta());
            generalResponse.setTipo(movement.getCuenta().getTipoCuenta());
            generalResponse.setSaldoIncial(movement.getCuenta().getSaldoInicial());
            generalResponse.setEstado(movement.getCuenta().getEstado());
            generalResponse.setMovimiento(movement.getValor());
            generalResponse.setSaldoDisponible(movement.getSaldo());
            movementsList.add(generalResponse);
        }
        return movementsList;
    }
}
