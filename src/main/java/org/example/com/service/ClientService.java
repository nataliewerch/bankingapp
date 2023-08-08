package org.example.com.service;

import org.example.com.entity.Client;

import java.util.List;

public interface ClientService {
    List<Client> getAllClients();

    Client createClient(Client client);
}
