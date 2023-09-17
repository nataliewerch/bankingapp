package org.example.com.service;

import org.example.com.entity.Account;
import org.example.com.entity.Client;
import org.example.com.entity.Manager;
import org.example.com.entity.enums.*;
import org.example.com.exception.ClientNotFoundException;
import org.example.com.exception.ManagerNotFoundException;
import org.example.com.repository.AccountRepository;
import org.example.com.repository.ClientRepository;
import org.example.com.repository.ManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    ClientServiceImpl clientService;
    private List<Client> clients;
    private List<Account> accounts;
    private List<Manager> managers;

    @BeforeEach
    void init() {
        managers = Arrays.asList(
                new Manager(1L, "Anton", "Antonov", ManagerStatus.ACTIVE, null, null, null, null, null),
                new Manager(2L, "Ivan", "Ivanov", ManagerStatus.ACTIVE, null, null, null, null, null),
                new Manager(3L, "Anna", "Stock", ManagerStatus.ACTIVE, null, null, null, null, null));

        clients = Arrays.asList(
                new Client(UUID.randomUUID(), ClientStatus.ACTIVE, "123456789", "Inna", "Scheff", "scheff@gmail", "32144 Bonn, Hoffmanstrasse 12", "+49 157 5454 6632", null, null, managers.get(0), null, null),
                new Client(UUID.randomUUID(), ClientStatus.ACTIVE, "987654321", "Artur", "Petrov", "petrov@gmail", "32132 Bonn, Alterstrasse 77", "+49 178 7732 1654", null, null, managers.get(1), null, null),
                new Client(UUID.randomUUID(), ClientStatus.ACTIVE, "112233445", "Stepan", "Stepanov", "stepanov@gmail", "32144 Bonn, Krommelstrasse 12", "+49 179 4567 3321", null, null, managers.get(2), null, null));

        accounts = Arrays.asList(
                new Account(UUID.randomUUID(), "Savings Accounts", AccountType.DEPOSIT, AccountStatus.ACTIVE, 3400.0, CurrencyCode.USD, null, null, null, clients.get(0), null, null),
                new Account(UUID.randomUUID(), "Credit Card", AccountType.CREDIT, AccountStatus.ACTIVE, 25000.0, CurrencyCode.EUR, null, null, null, clients.get(1), null, null),
                new Account(UUID.randomUUID(), "Debit Card", AccountType.DEPOSIT, AccountStatus.ACTIVE, 3400.0, CurrencyCode.EUR, null, null, null, clients.get(2), null, null));
    }

    @Test
    void getAll() {
        Mockito.when(clientRepository.findAll()).thenReturn(clients);
        assertEquals(3, clientService.getAll().size());
    }

    @Test
    void getAllWithNoClients() {
        Mockito.when(clientRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(ClientNotFoundException.class, () -> clientService.getAll());
    }

    @Test
    void getById() {
        UUID clientId = clients.get(0).getId();
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.ofNullable(clients.get(0)));
        Client result = clientService.getById(clientId);
        assertNotNull(result);
        assertEquals(clientId, result.getId());
        assertEquals(ClientStatus.ACTIVE, result.getStatus());
    }

    @Test
    void getByIdWhenClientsNotFound() {
        Mockito.when(clientRepository.findById(clients.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(ClientNotFoundException.class, () -> clientService.getById(clients.get(0).getId()));
    }

    @Test
    void getAllByStatus() {
        Mockito.when(clientRepository.getAllByStatus(clients.get(0).getStatus())).thenReturn(clients);
        List<Client> clientList = clientService.getAllByStatus(clients.get(0).getStatus());
        assertEquals(clients.size(), clientList.size());
        assertEquals(clients.get(0).getStatus(), clientList.get(0).getStatus());
    }

    @Test
    void getAllByStatusWhenClientsNotFound() {
        Mockito.when(clientRepository.getAllByStatus(clients.get(0).getStatus())).thenReturn(new ArrayList<>());
        assertThrows(ClientNotFoundException.class, () -> clientService.getAllByStatus(clients.get(0).getStatus()));
    }

    @Test
    void getAllByManagerId() {
        Mockito.when(managerRepository.findById(managers.get(0).getId())).thenReturn(Optional.of(managers.get(0)));
        Mockito.when(clientRepository.getAllByManager_Id(managers.get(0).getId())).thenReturn(clients);
        List<Client> clientList = clientService.getAllByManagerId(managers.get(0).getId());
        assertEquals(clients.size(), clientList.size());
    }

    @Test
    void getAllByManagerIdWhenManagerNotFound() {
        Mockito.when(managerRepository.findById(managers.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(ManagerNotFoundException.class, () -> clientService.getAllByManagerId(managers.get(0).getId()));
    }

    @Test
    void balance() {
        UUID clientId = clients.get(0).getId();
        UUID accountId = accounts.get(0).getId();
        clients.get(0).setAccounts(accounts);

        Mockito.when(clientRepository.getReferenceById(clientId)).thenReturn(clients.get(0));
        assertEquals(accounts.get(0).getBalance(), clientService.balance(clientId, accountId));
        Mockito.verify(clientRepository).getReferenceById(clients.get(0).getId());
    }

    @Test
    void create() {
        Long managerId = managers.get(0).getId();
        Client newClient = clients.get(0);
        Mockito.when(managerRepository.findById(managerId)).thenReturn(Optional.of(managers.get(0)));
        Mockito.when(clientRepository.save(newClient)).thenReturn(newClient);
        Client result = clientService.create(newClient, managerId);
        assertNotNull(result);
        assertEquals(managerId, result.getManager().getId());
        assertEquals(newClient.getId(), result.getId());
        assertEquals(newClient.getStatus(), result.getStatus());
    }

    @Test
    void createWhenManagerNotFound() {
        Mockito.when(managerRepository.findById(managers.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(ManagerNotFoundException.class, () -> clientService.create(clients.get(0), managers.get(0).getId()));
    }

    @Test
    void deleteById() {
        UUID clientId = clients.get(0).getId();
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.of(clients.get(0)));
        clientService.deleteById(clientId);
        Mockito.verify(clientRepository).deleteById(clientId);

    }

    @Test
    void deleteByIdWhenClientNotFound() {
        Mockito.when(clientRepository.findById(clients.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(ClientNotFoundException.class, () -> clientService.deleteById(clients.get(0).getId()));
    }

    @Test
    void changeStatus() {
        Mockito.when(clientRepository.findById(clients.get(0).getId())).thenReturn(Optional.ofNullable(clients.get(0)));
        Client newClient = clientService.changeStatus(clients.get(0).getId(), ClientStatus.BLOCKED);
        assertEquals(ClientStatus.BLOCKED, newClient.getStatus());
    }

    @Test
    void changeStatusWhenClientNotFound() {
        Mockito.when(clientRepository.findById(clients.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(ClientNotFoundException.class, () -> clientService.changeStatus(clients.get(0).getId(), ClientStatus.BLOCKED));
    }

}