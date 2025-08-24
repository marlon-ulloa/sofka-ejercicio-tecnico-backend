package com.sofka.msaccount.service;

import com.sofka.msaccount.dto.Response;
import com.sofka.msaccount.entity.Cuenta;
import com.sofka.msaccount.entity.Movimiento;
import com.sofka.msaccount.entity.Transaction;
import com.sofka.msaccount.exception.InsufficientFundsException;
import com.sofka.msaccount.exception.ResourceNotFoundException;
import com.sofka.msaccount.repository.AccountRepository;
import com.sofka.msaccount.repository.MovementRepository;
import com.sofka.msaccount.repository.TransactionRepository;
import com.sofka.msaccount.util.Conversions;
import com.sofka.msaccount.util.FormatData;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase que se encarga de la logica del negocio de movimientos y es intermediario entre el repository de movimientos con el controller
 */
@Service
public class MovementService {

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    @Autowired
    private Conversions convertions;

    @Autowired
    private TransactionRepository transactionRepository;

    public MovementService(MovementRepository movementRepository,
                             AccountRepository accountRepository) {
        this.movementRepository = movementRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Response createMovement(Movimiento movementRequest) {
        // Buscar la cuenta asociada
        Cuenta account = accountRepository.findByNumeroCuenta(movementRequest.getCuenta().getNumeroCuenta());
        if(account == null) {
            throw new ResourceNotFoundException("Cuenta no encontrada con número " + movementRequest.getCuenta().getNumeroCuenta());
        }

        // Calcular saldo según tipo de movimiento
        BigDecimal actualBalance = account.getSaldoActual();
        BigDecimal newBalance;
        if (movementRequest.getTipoMovimiento().equalsIgnoreCase("Deposito")) {
            newBalance = actualBalance.add(movementRequest.getValor());
        } else if (movementRequest.getTipoMovimiento().equalsIgnoreCase("Retiro")) {
            newBalance = actualBalance.subtract(movementRequest.getValor());
            if(newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientFundsException("Saldo no disponible");
            }
            movementRequest.setValor(convertions.toNegative(movementRequest.getValor()));
        } else if(movementRequest.getTipoMovimiento().equalsIgnoreCase("Apertura")) {
            newBalance = actualBalance;
        }
        else {
            throw new IllegalArgumentException("Tipo de movimiento inválido");
        }

        // Actualizar la cuenta y asignar datos al movimiento
        account.setSaldoActual(newBalance);
        movementRequest.setFecha(LocalDateTime.now());
        movementRequest.setSaldo(newBalance);
        movementRequest.setCuenta(account);

        Movimiento movimientoGuardado = movementRepository.save(movementRequest);

        /// Guarda transaccion
        Transaction transaction = new Transaction();
        transaction.setCuenta(account);
        transaction.setTipoMovimiento(movementRequest.getTipoMovimiento());
        transaction.setValor(movementRequest.getValor());
        transaction.setSaldoAnterior(actualBalance);
        transaction.setSaldoNuevo(newBalance);
        transactionRepository.save(transaction);
        
        Response response = FormatData.getInstance().formatResponse(HttpStatus.CREATED.value(), "Movimiento Creado : " + movementRequest.getTipoMovimiento() + " de " + (movementRequest.getValor()).abs(), movimientoGuardado);

        return response;
    }

    public List<Movimiento> getMovimientosByCuenta(String numeroCuenta) {
        return movementRepository.findByCuentaNumeroCuenta(numeroCuenta);
    }

    public List<Movimiento> getMovimientosByClienteAndFecha(Long clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movementRepository.findByCuentaClienteIdAndFechaBetween(clienteId, fechaInicio, fechaFin);
    }

}
