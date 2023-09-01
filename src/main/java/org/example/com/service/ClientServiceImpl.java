package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.converter.Converter;
import org.example.com.dto.AccountDto;
import org.example.com.dto.ClientDto;
import org.example.com.entity.Account;
import org.example.com.entity.Client;
import org.example.com.entity.Manager;
import org.example.com.entity.enums.ClientStatus;
import org.example.com.exception.AccountNotFoundException;
import org.example.com.exception.ClientNotFoundException;
import org.example.com.exception.ManagerNotFoundException;
import org.example.com.repository.AccountRepository;
import org.example.com.repository.ClientRepository;
import org.example.com.repository.ManagerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final AccountService accountService;
    private final ManagerRepository managerRepository;
    private final Converter<Client, ClientDto> clientDtoConverter;
    private final AccountRepository accountRepository;
    private final Converter<Account, AccountDto> accountDtoConverter;


    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Override
    public List<Client> getAll() {
        List<Client> clients = clientRepository.findAll();
        if (clients.isEmpty()) {
            throw new AccountNotFoundException("No clients found");
        }
        return clients;
    }

    @Override
    public Client getById(UUID id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(String.format("Client with id %s not found", id)));
    }

    @Override
    public List<Client> getAllByStatus(ClientStatus status) {
        List<Client> clients = clientRepository.getAllByStatus(status);
        if (clients.isEmpty()) {
            throw new ClientNotFoundException(String.format("Clients with status %s not found", status));
        }
        return clients;
    }

    @Override
    public ClientDto getClientWithAccounts(UUID clientId) {
        ClientDto clientDto =clientDtoConverter.toDto(clientRepository.findById(clientId)
                .orElseThrow(()->new ClientNotFoundException(String.format("Clients with status %s not found", clientId))));

        List<AccountDto> accountDtos = accountRepository.findAllByClientId(clientId).stream()
                        .map(account -> new AccountDto(
                                account.getName(),
                                account.getType(),
                                account.getStatus(),
                                account.getBalance(),
                                account.getCurrencyCode()))
                                .collect(Collectors.toList());
        clientDto.setAccounts(accountDtos);
        return clientDto;
    }

    @Override
    public Double balance(UUID clientId, UUID accountId) {
        return clientRepository.getReferenceById(clientId).getAccounts().stream()
                .filter(acc -> acc.getId().equals(accountId))
                .findFirst()
                .map(Account::getBalance)
                .orElseThrow(() -> new AccountNotFoundException(
                        String.format("Account with id: %s not found for the client with id: %s ", accountId, clientId)));
    }

    @Override
    public Client create(Client client, Long managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException(String.format("Manager with id %d not found: " + managerId)));
        client.setManager(manager);
        return clientRepository.save(client);

    }

    @Override
    @Transactional
    public void delete(Client client) {
        clientRepository.delete(client);
    }

    @Override
    public Client changeStatus(UUID id, ClientStatus newStatus) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(
                        String.format("Client with id: %s not found ", id)));
        client.setStatus(newStatus);
        return client;
    }

    @Override
    public void deleteById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(String.format("Client with id: %s not found ", id)));
        clientRepository.deleteById(id);
    }
}
