package com.sofka.msclient.service;

import com.sofka.msclient.dto.ClientDTO;
import com.sofka.msclient.entity.Client;
import com.sofka.msclient.exception.ResourceNotFoundException;
import com.sofka.msclient.repository.ClientRepository;
import com.sofka.msclient.util.FormatData;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Clase service que implementa logica de negocio y es un intermediario entre el repositorio y el controller
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * Constructor de la clase
     * @param clientRepository
     */
    public ClientService(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    /**
     * COnsulta del repository a todoa los clientes
     * @return retorna un listado de tipo CLienteDTO con todos los clientes
     */
    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return FormatData.getInstance().formatClientList(clients);
    }

    /**
     * Obtiene del repository un cliente de acuerdo a su id
     * @param id Indica el id del cliente
     * @return Retorna el cliente encontrado
     */
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id));
        return FormatData.getInstance().clientToClientDTO(client);
    }

    /**
     * Obtiene un cliente del repositorio en base a su identificacion
     * @param identification Indica la identificacion del cliente a buscar
     * @return Retorna el cliente encontrado
     */
    public Client getClientByIdentification(String identification) {
        return clientRepository.findByIdentificacion(identification);
    }

    /**
     * Metodo que crea un nuevo cliente
     * @param clientDTO Indica la informacion del cliente a crear
     * @return Retorna el DTO con el cliente creado
     */
    public ClientDTO createClient(ClientDTO clientDTO) {
        Client client = FormatData.getInstance().clientDTOtoClient(clientDTO);
        Client saved = clientRepository.save(client);
        return FormatData.getInstance().clientToClientDTO(saved);
    }

    /**
     * Actualiza la informacion de un cliente
     * @param id Indica el id del cliente a actualizar
     * @param clienteDetails Indica la informacion del cliente a actualizar
     * @return Retorna la informacion del cliente actualizado
     */
    public ClientDTO updateClient(Long id, ClientDTO clienteDetails) {
        ClientDTO client = getClientById(id);
        client.setNombre(clienteDetails.getNombre());
        client.setGenero(clienteDetails.getGenero());
        client.setEdad(clienteDetails.getEdad());
        client.setDireccion(clienteDetails.getDireccion());
        client.setTelefono(clienteDetails.getTelefono());
        client.setContrasena(clienteDetails.getContrasena());
        client.setEstado(clienteDetails.getEstado());
        Client clientToSave = FormatData.getInstance().clientDTOtoClient(client);
        Client clientUpdated = clientRepository.save(clientToSave);
        return FormatData.getInstance().clientToClientDTO(clientUpdated);
    }

    /**
     * Metodo que elimina de manera logica a un cliente
     * @param id Indica el id del cliente a eliminar
     */
    public void deleteClient(Long id) {
        ClientDTO client = getClientById(id);
        client.setEstado(false);//unicamente cambio el estado (borrado logico)
        Client clientToDelete = FormatData.getInstance().clientDTOtoClient(client);
        clientRepository.save(clientToDelete);
        //Ejecutar este codigo comentado si en verdad se requiere eliminar al cliente de la base de datos
        /*
        Client client = getClienteById(id);
        clientRepository.delete(client);
         */
    }
}
