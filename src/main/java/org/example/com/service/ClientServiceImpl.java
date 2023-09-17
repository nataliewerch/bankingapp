package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Account;
import org.example.com.entity.Client;
import org.example.com.entity.Manager;
import org.example.com.entity.enums.ClientStatus;
import org.example.com.exception.AccountNotFoundException;
import org.example.com.exception.ClientNotFoundException;
import org.example.com.exception.ManagerNotFoundException;
import org.example.com.repository.ClientRepository;
import org.example.com.repository.ManagerRepository;
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
    private final ManagerRepository managerRepository;

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
     * @param id - The unique identifier of the client.
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
     * @param status - The status of clients to filter by.
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
     * @param managerId - The unique identifier of the manager.
     * @return A list of Client objects managed by the specified manager.
     * @throws ManagerNotFoundException If the specified manager is not found in the database.
     * @throws ClientNotFoundException  If no clients are found for the specified manager.
     */
    @Override
    public List<Client> getAllByManagerId(Long managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException(String.format("Manager with id %d not found", managerId)));
        List<Client> clients = clientRepository.getAllByManager_Id(managerId);
        if (clients.isEmpty()) {
            throw new ClientNotFoundException(String.format("Client list is empty for manager with id %d", managerId));
        }
        return clients;
    }

    /**
     * Retrieves the balance of a client's account.
     *
     * @param clientId  - The unique identifier of the client.
     * @param accountId - The unique identifier of the client's account.
     * @return The balance of the client's account.
     * @throws AccountNotFoundException If the specified account is not found for the client.
     */
    @Override
    public Double balance(UUID clientId, UUID accountId) {
        return clientRepository.getReferenceById(clientId).getAccounts().stream()
                .filter(acc -> acc.getId().equals(accountId))
                .findFirst()
                .map(Account::getBalance)
                .orElseThrow(() -> new AccountNotFoundException(
                        String.format("Account with id: %s not found for the client with id: %s ", accountId, clientId)));
    }

    /**
     * Creates a new client and associates it with a manager.
     *
     * @param client    - The Client object to be created.
     * @param managerId - The unique identifier of the manager associated with the client.
     * @return The created Client object.
     * @throws ManagerNotFoundException If the specified manager is not found in the database.
     */
    @Override
    public Client create(Client client, Long managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException(String.format("Manager with id %d not found: ", managerId)));
        client.setManager(manager);
        return clientRepository.save(client);
    }

    /**
     * Changes the status of a client.
     *
     * @param id        - The unique identifier of the client.
     * @param newStatus - The new status to set for the client.
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
     * @param id - The unique identifier of the client to be deleted.
     * @throws ClientNotFoundException If the specified client is not found in the database.
     */
    @Override
    public void deleteById(UUID id) {
        clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(String.format("Client with id: %s not found ", id)));
        clientRepository.deleteById(id);
    }
}
