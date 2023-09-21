package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Client;
import org.example.com.entity.ClientProfile;
import org.example.com.entity.Manager;
import org.example.com.entity.enums.ClientStatus;
import org.example.com.exception.ClientNotFoundException;
import org.example.com.exception.ManagerNotFoundException;
import org.example.com.repository.ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of the ClientService interface for managing client-related operations.
 *
 * @author Natalie Werch
 */
@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ManagerService managerService;
    private final ClientProfileService clientProfileService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Retrieves a list of all clients.
     *
     * @return A list of Client objects.
     * @throws ClientNotFoundException If no clients are found in the database.
     */
    @Override
    public List<Client> getAll() {
        List<Client> clients = clientRepository.findAll();
        if (clients.isEmpty()) {
            throw new ClientNotFoundException("No clients found");
        }
        return clients;
    }

    /**
     * Retrieves a client by its unique identifier.
     *
     * @param id The unique identifier of the client.
     * @return The Client object with the specified ID.
     * @throws ClientNotFoundException If a client with the specified ID is not found in the database.
     */
    @Override
    public Client getById(UUID id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(String.format("Client with id %s not found", id)));
    }

    /**
     * Retrieves a list of clients by their status.
     *
     * @param status The status of clients to filter by.
     * @return A list of Client objects with the specified status.
     * @throws ClientNotFoundException If no clients with the specified status are found in the database.
     */
    @Override
    public List<Client> getAllByStatus(ClientStatus status) {
        List<Client> clients = clientRepository.getAllByStatus(status);
        if (clients.isEmpty()) {
            throw new ClientNotFoundException(String.format("Clients with status %s not found", status));
        }
        return clients;
    }

    /**
     * Retrieves a list of clients managed by a specific manager.
     *
     * @param managerId The unique identifier of the manager.
     * @return A list of Client objects managed by the specified manager.
     * @throws ManagerNotFoundException If the specified manager is not found in the database.
     * @throws ClientNotFoundException  If no clients are found for the specified manager.
     */
    @Override
    public List<Client> getAllByManagerId(Long managerId) {
        Manager manager = managerService.getById(managerId);
        List<Client> clients = clientRepository.getAllByManager_Id(managerId);
        if (clients.isEmpty()) {
            throw new ClientNotFoundException(String.format("Client list is empty for manager with id %d", managerId));
        }
        return clients;
    }

    /**
     * Creates a new client and associates it with a manager.
     *
     * @param client    The Client object to be created.
     * @param managerId The unique identifier of the manager associated with the client.
     * @return The created Client object.
     * @throws ManagerNotFoundException If the specified manager is not found in the database.
     */
    @Override
    public Client create(Client client, Long managerId, String login, String password) {
        Manager manager = managerService.getById(managerId);
        client.setManager(manager);
        clientRepository.save(client);
        clientProfileService.create(
                new ClientProfile(login, passwordEncoder.encode(password), client.getId()));
        return client;
    }

    /**
     * Changes the status of a client.
     *
     * @param id        The unique identifier of the client.
     * @param newStatus The new status to set for the client.
     * @return The updated Client object with the new status.
     * @throws ClientNotFoundException If the specified client is not found in the database.
     */
    @Override
    public Client changeStatus(UUID id, ClientStatus newStatus) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(
                        String.format("Client with id: %s not found ", id)));
        client.setStatus(newStatus);
        clientRepository.save(client);
        return client;
    }

    /**
     * Deletes a client by its unique identifier.
     *
     * @param id The unique identifier of the client to be deleted.
     * @throws ClientNotFoundException If the specified client is not found in the database.
     */
    @Override
    public void deleteById(UUID id) {
        clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(String.format("Client with id: %s not found ", id)));
        clientRepository.deleteById(id);
    }

    /**
     * Reassigns clients from one manager to another.
     *
     * @param sourceManagerId The unique identifier of the source manager.
     * @param targetManagerId The unique identifier of the target manager.
     * @throws ClientNotFoundException If no clients are found for reassignment.
     */
    @Override
    public void reassignClients(Long sourceManagerId, Long targetManagerId) {
        List<Client> clientsToReassign = clientRepository.getAllByManager_Id(sourceManagerId);
        Manager targetManager = managerService.getById(targetManagerId);

        if (clientsToReassign != null && !clientsToReassign.isEmpty()) {
            for (Client client : clientsToReassign) {
                client.setManager(targetManager);
                clientRepository.save(client);
            }
        } else {
            throw new ClientNotFoundException("No clients found");
        }
    }
}
