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
import org.example.com.repository.ClientRepository;
import org.example.com.repository.ManagerRepository;
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

    @Override
    public List<ClientDto> getAll() {
        List<Client> clients = clientRepository.findAll();
        if (clients.isEmpty()) {
            throw new AccountNotFoundException("No clients found");
        }
        return clients.stream()
                .map(clientDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDto getById(UUID id) {
        return clientDtoConverter.toDto(clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(String.format("Client with id %s not found", id))));
    }

    @Override
    public List<ClientDto> getAllByStatus(ClientStatus status) {
        List<Client> clients = clientRepository.getAllByStatus(status);
        if (clients.isEmpty()){
            throw new ClientNotFoundException(String.format("Clients with status %s not found", status));
        }
        return clients.stream()
                .map(clientDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDto getClientWithAccounts(UUID clientId) {
        ClientDto clientDto = getById(clientId);
        List<AccountDto> accounts = accountService.getByClientId(clientId);
        clientDto.setAccounts(accounts);
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
    public ClientDto create(ClientDto clientDto, Long managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException(String.format("Manager with id %d not found: " + managerId)));
        Client client = clientDtoConverter.toEntity(clientDto);
        client.setManager(manager);
        Client createdClient = clientRepository.save(client);
        return clientDtoConverter.toDto(createdClient);

    }

    @Override
    @Transactional
    public void delete(ClientDto clientDto) {
        clientRepository.delete(clientDtoConverter.toEntity(clientDto));
    }

    @Override
    public ClientDto changeStatus(UUID id, ClientStatus newStatus) {
//        ClientDto clientDto = getById(id);
//        clientDto.setStatus(newStatus);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(
                        String.format("Client with id: %s not found ", id)));
        return clientDtoConverter.toDto(client);
    }

    @Override
    public void deleteById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(String.format("Client with id: %s not found ", id)));
        clientRepository.deleteById(id);
    }
}
