package org.example.com.controller;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.ClientDto;
import org.example.com.entity.enums.ClientStatus;
import org.example.com.service.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("clients")
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    List<ClientDto> getAll() {
        return clientService.getAll();
    }

    @GetMapping("/id/{id}")
    ClientDto getById(@PathVariable(name = "id") UUID id) {
        return clientService.getById(id);
    }

    @GetMapping("/status/{status}")
    List<ClientDto> getAllByStatus(@PathVariable(name = "status") ClientStatus status) {
        return clientService.getAllByStatus(status);
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
        return clientService.create(clientDto, managerId);
    }

    @PostMapping("/change-status/{id}/{newStatus}")
    ClientDto changeStatus(@PathVariable(name = "id") UUID id, @PathVariable(name = "newStatus") ClientStatus newStatus) {
        return clientService.changeStatus(id, newStatus);
    }

    @DeleteMapping("/delete/{id}")
    void deleteClientById(@PathVariable(name = "id") UUID id) {
        clientService.deleteById(id);
    }
}
