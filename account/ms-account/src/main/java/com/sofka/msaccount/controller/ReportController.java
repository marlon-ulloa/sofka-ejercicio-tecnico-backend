package com.sofka.msaccount.controller;

import com.sofka.msaccount.dto.GeneralResponse;
import com.sofka.msaccount.entity.Cuenta;
import com.sofka.msaccount.entity.Movimiento;
import com.sofka.msaccount.messaging.AsyncConsumerService;
import com.sofka.msaccount.messaging.AsyncProducerService;
import com.sofka.msaccount.repository.AccountRepository;
import com.sofka.msaccount.service.MovementService;
import com.sofka.msaccount.util.FormatData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * CLase que espone los endpoints para la generacion del reporte de estado de cuentas
 */
@RestController
@RequestMapping("/api/reportes")
public class ReportController {
    @Autowired
    private AsyncProducerService asyncProducerService;
    @Autowired
    private AsyncConsumerService asyncConsumerService;

    private final AccountRepository accountRepository;
    private final MovementService movementService;

    public ReportController(AccountRepository accountRepository, MovementService movementService) {
        this.accountRepository = accountRepository;
        this.movementService = movementService;
    }

    /**
     * Metodo que expone el endoint que devuelve el estado de cuenta
     * @param clienteId Indica la identificacion del cliente
     * @param fechaInicio Indica la fecha de inicio del reporte
     * @param fechaFin Indica la fecha de fin del reporte
     * @return Retorna el estado de cuenta en base al rango de fechaas
     */
    @GetMapping
    public List<GeneralResponse> getEstadoCuenta(
            @RequestParam("clienteId") String clienteId,
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        //enviar información al cliente
        asyncProducerService.sendClienteRequest(clienteId);

        Map<String, Object> client = asyncConsumerService.obtenerRespuesta(clienteId);
        Long clientepk = Long.parseLong(client.get("id").toString());
        String clientName = client.get("nombre").toString();

        // Obtener las cuentas del cliente
        List<Cuenta> cuentas = accountRepository.findByClienteId(clientepk);

        // Obtener movimientos del cliente en el rango de fechas
        List<Movimiento> movimientos = movementService.getMovimientosByClienteAndFecha(clientepk, fechaInicio, fechaFin);

        return FormatData.getInstance().formatResponse(movimientos, clientName);
    }
}
