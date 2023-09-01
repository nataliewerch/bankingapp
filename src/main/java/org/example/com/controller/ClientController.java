package org.example.com.controller;

import lombok.RequiredArgsConstructor;
import org.example.com.converter.Converter;
import org.example.com.dto.ClientDto;
import org.example.com.entity.Client;
import org.example.com.entity.enums.ClientStatus;
import org.example.com.service.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("clients")
public class ClientController {

    private final ClientService clientService;
    private final Converter<Client, ClientDto> clientDtoConverter;

    @GetMapping
    List<ClientDto> getAll() {
        return clientService.getAll().stream()
                .map(clientDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/clientId/{clientId}")
    ClientDto getById(@PathVariable(name = "clientId") UUID clientId) {
        return clientDtoConverter.toDto(clientService.getById(clientId));
    }

    @GetMapping("/status/{status}")
    List<ClientDto> getAllByStatus(@PathVariable(name = "status") ClientStatus status) {
        return clientService.getAllByStatus(status).stream()
                .map(clientDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/accounts/{clientId}")
    ClientDto getClientWithAccounts(@PathVariable(name = "clientId") UUID clientId) {
        return clientService.getClientWithAccounts(clientId);
    }

    @GetMapping("/balance/{clientId}/{accountId}")
    Double getBalanceByClientIdAndAccountId(@PathVariable(name = "clientId") UUID clientId, @PathVariable(name = "accountId") UUID accountId) {
        return clientService.balance(clientId, accountId);
    }

    @PostMapping("/create/{managerId}")
    ClientDto createClient(@RequestBody ClientDto clientDto, @PathVariable(name = "managerId") Long managerId) {
        return clientDtoConverter.toDto(clientService.create(clientDtoConverter.toEntity(clientDto), managerId));
    }

    @PostMapping("/change-status/{id}/{newStatus}")
    ClientDto changeStatus(@PathVariable(name = "id") UUID id, @PathVariable(name = "newStatus") ClientStatus newStatus) {
        return clientDtoConverter.toDto(clientService.changeStatus(id, newStatus));
    }

    @DeleteMapping("/delete/{id}")
    void deleteClientById(@PathVariable(name = "id") UUID id) {
        clientService.deleteById(id);
    }
}
