package com.sofka.msclient.exception;

import com.sofka.msclient.dto.Response;
import com.sofka.msclient.util.FormatData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * CLase global para handler de las excepciones, captura las excepciones y retorna una respuesta
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateClientException.class)
    public ResponseEntity<com.sofka.msclient.dto.Response> handleInsufficientFundsException(DuplicateClientException ex) {
        Response response = FormatData.getInstance().formatResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response> handleAccountNotFoundException(ResourceNotFoundException ex) {
        Response response = FormatData.getInstance().formatResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationExceptions(MethodArgumentNotValidException ex) {
        final String[] errors = {""};
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors[0] += "|| " + error.getField() + ": " + error.getDefaultMessage());

        Response response = FormatData.getInstance().formatResponse(HttpStatus.BAD_REQUEST.value(), errors[0]);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MessageNotSentException.class)
    public ResponseEntity<Response> handleMessageNotSendException(MessageNotSentException ex) {
        Response response = FormatData.getInstance().formatResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler(TimeOutException.class)
    public ResponseEntity<Response> handleTimeOutException(TimeOutException ex) {
        Response response = FormatData.getInstance().formatResponse(HttpStatus.REQUEST_TIMEOUT.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
    }

}
