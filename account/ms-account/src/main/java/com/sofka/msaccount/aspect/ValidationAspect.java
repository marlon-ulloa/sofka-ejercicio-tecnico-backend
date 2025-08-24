package com.sofka.msaccount.aspect;

import com.sofka.msaccount.dto.AccountDTO;
import com.sofka.msaccount.util.Validation;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Clase Aspect, que se ejecutara antes del proceso del webservice de creacion del cliente
 * Valida si existe o no el cliente para crear una cuenta
 */
@Aspect
@Component
public class ValidationAspect {

    @Autowired
    private Validation validation;

    @Before("@annotation(com.sofka.msaccount.validation.ValidateClientExists) && args(accountDTO,..)")
    public void validateClientBefore(JoinPoint joinPoint, AccountDTO accountDTO) {
        Long clientId = validation.validateClientExists(accountDTO.getIdentification());
        accountDTO.setClienteId(clientId);
    }
}
