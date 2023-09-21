package org.example.com.service;

import org.example.com.entity.Client;
import org.example.com.entity.Manager;
import org.example.com.entity.Product;
import org.example.com.entity.enums.ClientStatus;
import org.example.com.entity.enums.CurrencyCode;
import org.example.com.entity.enums.ManagerStatus;
import org.example.com.entity.enums.ProductStatus;
import org.example.com.exception.ManagerNotFoundException;
import org.example.com.repository.ManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ManagerServiceImplTest {

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private ManagerProfileService managerProfileService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ManagerServiceImpl managerService;
    private List<Product> products;
    private List<Manager> managers;
    private List<Client> clients;

    @BeforeEach
    void init() {
        managers = Arrays.asList(
                new Manager(1L, "Anton", "Antonov", ManagerStatus.ACTIVE, null, null, new ArrayList<>(), new ArrayList<>(), null),
                new Manager(2L, "Ivan", "Ivanov", ManagerStatus.ACTIVE, null, null, new ArrayList<>(), new ArrayList<>(), null),
                new Manager(3L, "Anna", "Stock", ManagerStatus.ACTIVE, null, null, new ArrayList<>(), new ArrayList<>(), null));


        clients = Arrays.asList(
                new Client(UUID.randomUUID(), ClientStatus.ACTIVE, "123456789", "Inna", "Scheff", "scheff@gmail", "32144 Bonn, Hoffmanstrasse 12", "+49 157 5454 6632", null, null, managers.get(0), null, null),
                new Client(UUID.randomUUID(), ClientStatus.ACTIVE, "987654321", "Artur", "Petrov", "petrov@gmail", "32132 Bonn, Alterstrasse 77", "+49 178 7732 1654", null, null, managers.get(1), null, null),
                new Client(UUID.randomUUID(), ClientStatus.ACTIVE, "112233445", "Stepan", "Stepanov", "stepanov@gmail", "32144 Bonn, Krommelstrasse 12", "+49 179 4567 3321", null, null, managers.get(2), null, null));


        products = Arrays.asList(
                new Product(1L, "Savings Accounts", ProductStatus.ACTIVE, CurrencyCode.USD, new BigDecimal("0.00"), 1000, null, null, managers.get(0), null),
                new Product(2L, "Credit Card", ProductStatus.ACTIVE, CurrencyCode.EUR, new BigDecimal("0.05"), 25000, null, null, managers.get(1), null),
                new Product(1L, "Debit Card", ProductStatus.ACTIVE, CurrencyCode.EUR, new BigDecimal("0.00"), 7000, null, null, managers.get(2), null));
    }


    @Test
    void getAll() {
        Mockito.when(managerRepository.findAll()).thenReturn(managers);
        assertEquals(3, managerService.getAll().size());
    }

    @Test
    void getAllWithNoManagers() {
        Mockito.when(managerRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(ManagerNotFoundException.class, () -> managerService.getAll());
    }

    @Test
    void getById() {
        Long managerId = managers.get(0).getId();
        Mockito.when(managerRepository.findById(managerId)).thenReturn(Optional.ofNullable(managers.get(0)));
        Manager result = managerService.getById(managerId);
        assertNotNull(result);
        assertEquals(managerId, result.getId());
        assertEquals(managers.get(0).getStatus(), result.getStatus());
    }

    @Test
    void getByIdWhenManagerNotFound() {
        Mockito.when(managerRepository.findById(managers.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(ManagerNotFoundException.class, () -> managerService.getById(managers.get(0).getId()));
    }

    @Test
    void create() {
        String login = "login";
        String password = "password";
        Mockito.when(managerRepository.save(managers.get(0))).thenReturn(managers.get(0));
        Manager result = managerService.create(managers.get(0), login, password);
        assertNotNull(result);
        assertEquals(managers.get(0).getId(), result.getId());
        assertEquals(managers.get(0).getStatus(), result.getStatus());
    }

    @Test
    void deleteById() {
        Mockito.when(managerRepository.findById(managers.get(0).getId())).thenReturn(Optional.of(managers.get(0)));
        managerService.deleteById(managers.get(0).getId());
        Mockito.verify(managerRepository).deleteById(managers.get(0).getId());
    }

    @Test
    void deleteByIdWhenClientNotFound() {
        Mockito.when(managerRepository.findById(managers.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(ManagerNotFoundException.class, () -> managerService.deleteById(managers.get(0).getId()));
    }
}