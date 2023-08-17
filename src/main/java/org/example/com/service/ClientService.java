package org.example.com.service;

import org.example.com.entity.Client;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    List<Client> getAllClients();

    Client getById(UUID id);

    Client createClient(Client client);

    Double balance(UUID clientId, UUID accountId);

    void delete(Client client);

    void deleteById(UUID id);
}
