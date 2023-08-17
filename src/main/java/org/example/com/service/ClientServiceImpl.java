package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Account;
import org.example.com.entity.Client;
import org.example.com.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client getById(UUID id) {
        return clientRepository.getReferenceById(id);
    }


    @Override
    public Client createClient(Client client) {
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
    public void deleteById(UUID id) {
        clientRepository.deleteById(id);
    }
}
