package org.example.com.controller;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Client;
import org.example.com.service.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("clients")
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<Client> getAll() {
        return clientService.getAllClients();
    }

    @PostMapping()
    public void create(@RequestBody Client client) {
        clientService.createClient(client);
    }
}
