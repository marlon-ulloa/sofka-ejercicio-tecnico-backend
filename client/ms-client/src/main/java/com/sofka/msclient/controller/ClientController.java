package com.sofka.msclient.controller;

import com.sofka.msclient.dto.ClientDTO;
import com.sofka.msclient.dto.Response;
import com.sofka.msclient.entity.Client;
import com.sofka.msclient.exception.ResourceNotFoundException;
import com.sofka.msclient.messaging.AsyncAccountService;
import com.sofka.msclient.service.ClientService;
import com.sofka.msclient.util.FormatData;
import com.sofka.msclient.util.Validation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    private AsyncAccountService asyncAccountService;

    @Autowired
    Validation validation;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Servicio para obtener todos los clientes
     * @return Retorna un listado de los clientes exitentes
     */
    @GetMapping
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients();
    }

    /**
     * Servicio para obtener un cliente por su id
     * @param id Indica el id del cliente
     * @return Retorna el cliente encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        ClientDTO client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    /**
     * Servicio que crea un cliente
     * @param client Indica los datos de entrada para al creacion de un cliente
     * @return Retorna la informacion del nuevo cliente creado
     */
    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO client) {
        try {
            ClientDTO nuevoCliente = clientService.createClient(client);
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
        }catch (DataIntegrityViolationException ex){
            validation.validateException(ex, client.getIdentificacion());
            // Si no es una violación de clave única, relanza la excepción original
            throw ex;
        }
    }

    /**
     * Servicio que actualiza la informacion del cliente
     * @param id Indica el id del cliente a actualizar
     * @param clienteDetails Indica los datos a actualizar
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable("id") Long id, @Valid @RequestBody ClientDTO clienteDetails) {
        ClientDTO updated = clientService.updateClient(id, clienteDetails);
        return ResponseEntity.ok(updated);
    }

    /**
     * Servicio para realizar la eliminacion del cliente.
     * Como un cliente puede tener una o varias cuentas, no se puede eliminar de la base de datos,
     * lo que se realiza es un borrado logico del cliente
     * @param id Indica el id del cliente a borrar
     * @return Retorna si se elimino u ocurrio una excepcion al momento de eliminar el cliente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteClient(@PathVariable("id") Long id) {
        //Desactivar todas las cuentas del cliente
        //enviar la senal al servicio de cuentas
        asyncAccountService.sendAccountDeleteRequest(id);
        Map<String, Object> result = asyncAccountService.obtainResponse(id);
        if (result.containsKey("error")) {
            throw new ResourceNotFoundException(result.get("error").toString());
        }
        //borrado logico del cliente
        clientService.deleteClient(id);
        return ResponseEntity.ok(FormatData.getInstance().formatResponse(HttpStatus.OK.value(), "Se ha eliminado con exito"));

    }

    /**
     * Servicio para obtener un cliente por su identificacion
     * @param identification Indica la identificacion del cliente
     * @return Retorna el cliente encontrado
     */
    @GetMapping("/getbyidentification")
    public ResponseEntity<ClientDTO> getClientByIdentification(@RequestParam("identification") String identification) {
        Client client = clientService.getClientByIdentification(identification);
        return ResponseEntity.ok(FormatData.getInstance().clientToClientDTO(client));
    }
}
