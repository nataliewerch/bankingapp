package org.example.com.controller;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.ClientDto;
import org.example.com.entity.Client;
import org.example.com.entity.Manager;
import org.example.com.entity.enums.ClientStatus;
import org.example.com.service.ClientService;
import org.example.com.converter.Converter;
import org.example.com.service.ManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    private final ManagerService managerService;
    private final Converter<Client, ClientDto> converter;
    private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);

    @GetMapping
    List<ClientDto> getAll() {
        return clientService.getAll().stream()
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
        try {
            logger.info("ЗАПРОС ПОЛУЧЕН: {}", clientDto);
            Manager manager = managerService.getById(clientDto.getManager().getId());

            Client client = converter.toEntity(clientDto);
            client.setManager(manager);
            Client createdClient = clientService.create(client);
            ClientDto createdClientDto = converter.toDto(createdClient);

            logger.info("ОПЕРАЦИЯ ПРОШЛА УСПЕШНО: {}", client);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdClientDto);
        } catch (Exception e) {
            logger.error("ОШИБКААА", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/change-status/{id}/{newStatus}")
    ResponseEntity<ClientDto> changeStatus(@PathVariable(name = "id") UUID id, @PathVariable(name = "newStatus") ClientStatus newStatus) {
        ClientDto clientDto = converter.toDto(clientService.changeStatus(id, newStatus));
        if (clientDto != null) {
            return new ResponseEntity<>(clientDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
