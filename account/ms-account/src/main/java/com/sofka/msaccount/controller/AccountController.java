package com.sofka.msaccount.controller;

import com.sofka.msaccount.dto.AccountDTO;
import com.sofka.msaccount.dto.Response;
import com.sofka.msaccount.entity.Cuenta;
import com.sofka.msaccount.entity.Movimiento;
import com.sofka.msaccount.service.AccountService;
import com.sofka.msaccount.service.MovementService;
import com.sofka.msaccount.util.FormatData;
import com.sofka.msaccount.validation.ValidateClientExists;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase que expone los endpoints a ser consumidos
 */
@RestController
@RequestMapping("/cuentas")
public class AccountController {
    private final AccountService accountService;
    private final MovementService movementService;

    private FormatData formatData;
    public AccountController(AccountService accountService, MovementService movementService) {
        this.accountService = accountService;
        this.movementService = movementService;
        formatData = new FormatData();
    }

    /**
     * CLase que devuelve al endpoint todas las cuentas
     * @return Retorna todas las cuentas
     */
    @GetMapping
    public List<Cuenta> getAllCuentas() {
        return accountService.getAllCuentas();
    }

    /**
     * Clase que obtiene una cuenta especifica de acuerdo al id indicado
     * @param id Indica el id de la cuenta
     * @return Retorna la cuenta encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getCuentaById(@PathVariable Long id) {
        AccountDTO accountDTO = accountService.getCuentaById(id);
        return ResponseEntity.ok(accountDTO);
    }

    /**
     * CLase que expone el endpoint para la creacion de una cuenta
     * @param accountDTO Informacion de la cuenta a crear
     * @return
     */
    @PostMapping
    @ValidateClientExists
    public ResponseEntity<Response> createCuenta(@Valid @RequestBody AccountDTO accountDTO) {
        System.out.println(accountDTO.getIdentification());
        //String clientId = validation.validateClientExists(accountDTO.getIdentification());

        Response newAccount = accountService.createCuenta(accountDTO);
        //Crea el movimiento inicial de la cuenta
        Movimiento movement = formatData.createMovimentInstance((Cuenta) newAccount.getData());
        Response newMovement = movementService.createMovement(movement);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    /**
     * Clase que actualiza una cuenta especifica de acuerdo al id indicado
     * @param id Indica el id de la cuenta a actualziar
     * @param accountDTO Indica la informacion a actualizar de la cuenta
     * @return Retorna la cuenta actualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateCuenta(@PathVariable("id") Long id, @RequestBody AccountDTO accountDTO) {
        Response actualizada = accountService.updateCuenta(id, accountDTO);
        return ResponseEntity.ok(actualizada);
    }
}
