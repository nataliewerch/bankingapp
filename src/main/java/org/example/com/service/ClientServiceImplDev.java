package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Client;
import org.example.com.repository.ClientRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Profile("dev")
public class ClientServiceImplDev implements ClientService {

    private final ClientRepository clientRepository;
    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }
}
