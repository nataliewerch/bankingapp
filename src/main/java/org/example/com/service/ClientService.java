package org.example.com.service;

import org.example.com.entity.Client;
import org.example.com.entity.enums.ClientStatus;

import java.util.List;
import java.util.UUID;

/**
 * This interface defines the contract for managing client-related operations.
 *
 * @author Natalie Werch
 */
public interface ClientService {

    List<Client> getAll();

    Client getById(UUID id);

    List<Client> getAllByStatus(ClientStatus status);

    List<Client> getAllByManagerId(Long managerId);

    Client create(Client clientDto, Long managerId, String login, String password);

    Client changeStatus(UUID id, ClientStatus newStatus);

    void deleteById(UUID id);

    void reassignClients(Long sourceManagerId, Long targetManagerId);
}
