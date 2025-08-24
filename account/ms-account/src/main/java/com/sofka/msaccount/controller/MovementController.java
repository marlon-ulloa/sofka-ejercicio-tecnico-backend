package com.sofka.msaccount.controller;

import com.sofka.msaccount.dto.Response;
import com.sofka.msaccount.entity.Movimiento;
import com.sofka.msaccount.service.MovementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase que expone los endpoints para los movimientos
 */
@RestController
@RequestMapping("/movimientos")
public class MovementController {
    private final MovementService movementService;

    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    /**
     * Metodo que expone el endpoint para crear un nuevo movimiento
     * @param movement Indica la informacion del movimiento a crear
     * @return Retorna el movimiento creado
     */
    @PostMapping()
    public ResponseEntity<Response> createMovement( @RequestBody Movimiento movement) {
        Response response = movementService.createMovement(movement);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Metodo que expone el endpoint de todos los movimientos en base al numero de cuenta dado
     * @param numeroCuenta
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Movimiento>> getMovementByAccount(@RequestParam("numeroCuenta") String numeroCuenta){
        List<Movimiento> movements = movementService.getMovimientosByCuenta(numeroCuenta);
        return new ResponseEntity<>(movements, HttpStatus.FOUND);

    }

}
