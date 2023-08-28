package org.example.com.service;

import org.example.com.dto.ClientDto;
import org.example.com.entity.enums.ClientStatus;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    List<ClientDto> getAll();

    ClientDto getById(UUID id);

    List<ClientDto> getAllByStatus(ClientStatus status);

    ClientDto getClientWithAccounts(UUID clientId);

    ClientDto create(ClientDto clientDto, Long managerId);

   Double balance(UUID clientId, UUID accountId);

    void delete(ClientDto clientDto);

    ClientDto changeStatus(UUID id, ClientStatus newStatus);


    void deleteById(UUID id);
}
