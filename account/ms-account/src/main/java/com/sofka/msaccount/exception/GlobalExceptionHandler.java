package com.sofka.msaccount.exception;

import com.sofka.msaccount.dto.Response;
import com.sofka.msaccount.util.FormatData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Clase para capturar las excepciones y retornar una respuesta de acuerdo a cada excepcion
 */
@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler({InsufficientFundsException.class, IllegalArgumentException.class})
    public ResponseEntity<Response> handleInsufficientFundsException(InsufficientFundsException ex) {
        Response response = FormatData.getInstance().formatResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response> handleAccountNotFoundException(ResourceNotFoundException ex) {
        Response response = FormatData.getInstance().formatResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationExceptions(MethodArgumentNotValidException ex) {
        System.out.println("Ingresaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        final String[] errors = {""};
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors[0] += "|| " + error.getField() + ": " + error.getDefaultMessage());

        Response response = FormatData.getInstance().formatResponse(HttpStatus.BAD_REQUEST.value(), errors[0], null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TimeOutException.class)
    public ResponseEntity<Response> handleTimeOutException(TimeOutException ex) {
        Response response = FormatData.getInstance().formatResponse(HttpStatus.REQUEST_TIMEOUT.value(), ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
    }
    @ExceptionHandler(MessageNotSentException.class)
    public ResponseEntity<Response> handleMessageNotSendException(MessageNotSentException ex) {
        Response response = FormatData.getInstance().formatResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

}
