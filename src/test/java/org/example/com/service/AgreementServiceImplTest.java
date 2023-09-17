package org.example.com.service;

import org.example.com.entity.*;
import org.example.com.entity.enums.*;
import org.example.com.exception.AccountNotFoundException;
import org.example.com.exception.AgreementNotFoundException;
import org.example.com.exception.ProductNotFoundException;
import org.example.com.repository.AccountRepository;
import org.example.com.repository.AgreementRepository;
import org.example.com.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AgreementServiceImplTest {

    @Mock
    private AgreementRepository agreementRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private AgreementServiceImpl agreementService;

    private List<Agreement> agreements;
    private List<Account> accounts;
    private List<Product> products;


    @BeforeEach
    void init() {
        List<Manager> managers = Arrays.asList(
                new Manager(1L, "Anton", "Antonov", ManagerStatus.ACTIVE, null, null, null, null, null),
                new Manager(2L, "Ivan", "Ivanov", ManagerStatus.ACTIVE, null, null, null, null, null),
                new Manager(3L, "Anna", "Stock", ManagerStatus.ACTIVE, null, null, null, null, null));

        List<Client> clients = Arrays.asList(
                new Client(UUID.randomUUID(), ClientStatus.ACTIVE, "123456789", "Inna", "Scheff", "scheff@gmail", "32144 Bonn, Hoffmanstrasse 12", "+49 157 5454 6632", null, null, managers.get(0), null, null),
                new Client(UUID.randomUUID(), ClientStatus.ACTIVE, "987654321", "Artur", "Petrov", "petrov@gmail", "32132 Bonn, Alterstrasse 77", "+49 178 7732 1654", null, null, managers.get(1), null, null),
                new Client(UUID.randomUUID(), ClientStatus.ACTIVE, "112233445", "Stepan", "Stepanov", "stepanov@gmail", "32144 Bonn, Krommelstrasse 12", "+49 179 4567 3321", null, null, managers.get(2), null, null));

        accounts = Arrays.asList(
                new Account(UUID.randomUUID(), "Savings Accounts", AccountType.DEPOSIT, AccountStatus.ACTIVE, 3400.0, CurrencyCode.USD, null, null, null, clients.get(0), null, null),
                new Account(UUID.randomUUID(), "Credit Card", AccountType.CREDIT, AccountStatus.ACTIVE, 25000.0, CurrencyCode.EUR, null, null, null, clients.get(1), null, null),
                new Account(UUID.randomUUID(), "Debit Card", AccountType.DEPOSIT, AccountStatus.ACTIVE, 3400.0, CurrencyCode.EUR, null, null, null, clients.get(2), null, null));

        products = Arrays.asList(
                new Product(1L, "Savings Accounts", ProductStatus.ACTIVE, CurrencyCode.USD, new BigDecimal("0.00"), 1000, null, null, managers.get(0), null),
                new Product(2L, "Credit Card", ProductStatus.ACTIVE, CurrencyCode.EUR, new BigDecimal("0.05"), 25000, null, null, managers.get(1), null),
                new Product(1L, "Debit Card", ProductStatus.ACTIVE, CurrencyCode.EUR, new BigDecimal("0.00"), 7000, null, null, managers.get(2), null));

        agreements = Arrays.asList(
                new Agreement(1L, new BigDecimal("0.00"), AgreementStatus.ACTIVE, 1000.0, null, null, accounts.get(0), products.get(0)),
                new Agreement(2L, new BigDecimal("0.05"), AgreementStatus.ACTIVE, 25000.0, null, null, accounts.get(0), products.get(0)),
                new Agreement(2L, new BigDecimal("0.00"), AgreementStatus.ACTIVE, 7000.0, null, null, accounts.get(0), products.get(0)));
    }


    @Test
    void getAll() {
        Mockito.when(agreementRepository.findAll()).thenReturn(agreements);
        assertEquals(3, agreementService.getAll().size());
    }

    @Test
    void getAllWithNoAgreements() {
        Mockito.when(agreementRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(AgreementNotFoundException.class, () -> agreementService.getAll());
    }

    @Test
    void getById() {
        Long agreementId = agreements.get(0).getId();
        Mockito.when(agreementRepository.findById(agreementId)).thenReturn(Optional.ofNullable(agreements.get(0)));
        Agreement result = agreementService.getById(agreementId);
        assertNotNull(result);
        assertEquals(agreementId, result.getId());
        assertEquals(AgreementStatus.ACTIVE, result.getStatus());
        assertEquals(1000.0, result.getSum());
    }

    @Test
    void getByIdWhenAgreementNotFound() {
        Mockito.when(agreementRepository.findById(agreements.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(AgreementNotFoundException.class, () -> agreementService.getById(agreements.get(0).getId()));
    }

    @Test
    void create() {
        Mockito.when(accountRepository.findById(accounts.get(0).getId())).thenReturn(Optional.of(accounts.get(0)));
        Mockito.when(productRepository.findById(products.get(0).getId())).thenReturn(Optional.of(products.get(0)));
        Mockito.when(agreementRepository.save(agreements.get(0))).thenReturn(agreements.get(0));

        Agreement result = agreementService.create(agreements.get(0), accounts.get(0).getId(), products.get(0).getId());

        assertEquals(agreements.get(0), result);
        assertEquals(accounts.get(0).getId(), result.getAccount().getId());
        assertEquals(agreements.get(0).getId(), result.getId());
        assertEquals(agreements.get(0).getSum(), result.getSum());
    }

    @Test
    void createAccountNotFound() {
        UUID accountId = UUID.randomUUID();
        Long productId = products.get(0).getId();
        Agreement newAgreement = agreements.get(1);

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> agreementService.create(newAgreement, accountId, productId));
    }

    @Test
    void createProductNotFound() {
        UUID accountId = accounts.get(0).getId();
        Long productId = 111L;
        Agreement newAgreement = new Agreement(4L, new BigDecimal("3.0"), AgreementStatus.ACTIVE, 2567.0, null, null, accounts.get(0), products.get(0));

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(accounts.get(0)));
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> agreementService.create(newAgreement, accountId, productId));
    }

    @Test
    void deleteById() {
        Long agreementId = agreements.get(0).getId();
        Mockito.when(agreementRepository.findById(agreementId)).thenReturn(Optional.of(agreements.get(0)));
        agreementService.deleteById(agreementId);
        Mockito.verify(agreementRepository).deleteById(agreementId);
    }

    @Test
    void deleteByIdAgreementNotFound() {
        Long nonExistingAgreementId = 999L;
        Mockito.when(agreementRepository.findById(nonExistingAgreementId)).thenReturn(Optional.empty());
        assertThrows(AgreementNotFoundException.class, () -> agreementService.deleteById(nonExistingAgreementId));
        Mockito.verify(agreementRepository, Mockito.never()).deleteById(nonExistingAgreementId);
    }
}