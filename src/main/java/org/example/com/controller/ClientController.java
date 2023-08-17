package org.example.com.controller;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.ClientDto;
import org.example.com.entity.Client;
import org.example.com.service.ClientService;
import org.example.com.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("clients")
public class ClientController {

    private final ClientService clientService;
    private final Converter<Client, ClientDto> converter;

    @GetMapping
    List<ClientDto> getAll() {
        return clientService.getAllClients().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    ClientDto getById(@PathVariable(name = "id") UUID id) {
        return converter.toDto(clientService.getById(id));
    }

    @GetMapping("/balance/{clientId}/{accountId}")
    Double getBalance(@PathVariable(name = "clientId") UUID clientId, @PathVariable(name = "accountId") UUID accountId) {
        return clientService.balance(clientId, accountId);
    }

    @PostMapping
    ResponseEntity<ClientDto> create(@RequestBody ClientDto clientDto) {

        return ResponseEntity.ok(converter.toDto(clientService.createClient(converter.toEntity(clientDto))));
    }

    @DeleteMapping("/delete")
    void deleteAccount(@RequestBody Client client) {
        clientService.delete(client);
    }

    @DeleteMapping("/delete/{id}")
    void deleteAccountById(@PathVariable(name = "id") UUID id) {
        clientService.deleteById(id);
    }
}
