package org.example.com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.com.converter.Converter;
import org.example.com.dto.ClientDto;
import org.example.com.entity.Account;
import org.example.com.entity.Client;
import org.example.com.entity.enums.AccountStatus;
import org.example.com.entity.enums.AccountType;
import org.example.com.entity.enums.ClientStatus;
import org.example.com.entity.enums.CurrencyCode;
import org.example.com.exception.ClientNotFoundException;
import org.example.com.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @MockBean
    private ClientService clientService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private ManagerService managerService;

    @MockBean
    private ProductService productService;

    @MockBean
    private AgreementService agreementService;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private Converter<Client, ClientDto> converter;

    @MockBean
    private ClientProfileService clientProfileService;

    @MockBean
    private ManagerProfileService managerProfileService;

    @Autowired
    private MockMvc mockMvc;

    private Client client;
    private ClientDto clientDto;


    @BeforeEach
    void init() {
        client = new Client(UUID.randomUUID(), ClientStatus.ACTIVE, null, "Inna", "Scheff", "scheff@gmail", null, "+49 157 5454 6632", null, null, null, List.of(
                new Account(UUID.randomUUID(), "Savings Accounts", AccountType.DEPOSIT, AccountStatus.ACTIVE, 3400.0, CurrencyCode.USD, null, null, null, null, null, null)),
                null);

        clientDto = new ClientDto(client.getFirstName(), client.getLastName(), client.getStatus());
    }

    @Test
    void getAll() throws Exception {
        Mockito.when(clientService.getAll()).thenReturn(List.of(client));
        Mockito.when(converter.toDto(client)).thenReturn(clientDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/clients").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(List.of(clientDto))));
    }

    @Test
    void getById() throws Exception {
        Mockito.when(clientService.getById(client.getId())).thenReturn(client);
        Mockito.when(converter.toDto(client)).thenReturn(clientDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/clients/" + client.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(clientDto)));
    }

    @Test
    void getByIdWhenIdIsNotExists() throws Exception {
        Mockito.when(clientService.getById(client.getId())).thenThrow(ClientNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/clients/{clientId}", client.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ClientNotFoundException));
    }

    @Test
    void getAllByStatus() throws Exception {
        Mockito.when(clientService.getAllByStatus(ClientStatus.ACTIVE)).thenReturn(List.of(client));
        Mockito.when(converter.toDto(client)).thenReturn(clientDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/clients/status/{status}", ClientStatus.ACTIVE.toString()).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(List.of(clientDto))));
    }

    @Test
    void getAllByStatusWhenClientNotFound() throws Exception {
        Mockito.when(clientService.getAllByStatus(client.getStatus())).thenThrow(ClientNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/clients/status/{status}", ClientStatus.ACTIVE.toString()).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ClientNotFoundException));
    }

    @Test
    void changeStatus() throws Exception {
        ClientStatus newStatus = ClientStatus.BLOCKED;
        Mockito.when(clientService.changeStatus(client.getId(), newStatus)).thenReturn(client);

        mockMvc.perform(MockMvcRequestBuilders.post("/clients/change-status/{id}/{newStatus}", client.getId(), newStatus)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void changeStatusWhenClientNotFound() throws Exception {
        ClientStatus newStatus = ClientStatus.BLOCKED;
        Mockito.when(clientService.changeStatus(client.getId(), newStatus)).thenThrow(ClientNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/clients/change-status/{id}/{newStatus}", client.getId(), newStatus)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ClientNotFoundException));
    }

    @Test
    void deleteClientById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/clients/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    void deleteClientByIdWhenClientNotFound() throws Exception {
        Mockito.doThrow(ClientNotFoundException.class)
                .when(clientService)
                .deleteById(client.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/clients/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ClientNotFoundException));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}