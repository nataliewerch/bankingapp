package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Account;
import org.example.com.entity.Client;
import org.example.com.entity.enums.ClientStatus;
import org.example.com.exception.ClientNotFoundException;
import org.example.com.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client getById(UUID id) {
        return clientRepository.getReferenceById(id);
    }


    @Override
    public Client create(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Double balance(UUID clientId, UUID accountId) {
        return getById(clientId).getAccounts().stream()
                .filter(acc -> acc.getId().equals(accountId))
                .findFirst()
                .map(Account::getBalance)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    @Override
    public void delete(Client client) {
        clientRepository.delete(client);
    }

    @Override
    public Client changeStatus(UUID id, ClientStatus newStatus) {
        Client client = getById(id);
        if (client == null) {
            throw new ClientNotFoundException("Client not found");
        }
        client.setStatus(newStatus);
        return create(client);
    }

    @Override
    public void deleteById(UUID id) {
        clientRepository.deleteById(id);
    }
}
