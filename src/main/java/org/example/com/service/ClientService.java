package org.example.com.service;

import org.example.com.entity.Client;
import org.example.com.entity.enums.ClientStatus;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    List<Client> getAll();

    Client getById(UUID id);

    Client create(Client client);

    Double balance(UUID clientId, UUID accountId);

    void delete(Client client);

    Client changeStatus(UUID id, ClientStatus newStatus);

    void deleteById(UUID id);
}
