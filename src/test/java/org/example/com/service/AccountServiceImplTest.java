package org.example.com.service;

import org.example.com.entity.Account;
import org.example.com.entity.Client;
import org.example.com.entity.Manager;
import org.example.com.entity.Transaction;
import org.example.com.entity.enums.*;
import org.example.com.exception.AccountNotFoundException;
import org.example.com.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private AccountServiceImpl accountService;

    private List<Client> clients;
    private List<Account> accounts;

    @BeforeEach
    void init() {
        List<Manager> managers = Arrays.asList(
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
        Mockito.when(accountRepository.findAll()).thenReturn(accounts);
        assertEquals(3, accountService.getAll().size());
    }

    @Test
    void getAllWithNoAccounts() {
        Mockito.when(accountRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(AccountNotFoundException.class, () -> accountService.getAll());
    }


    @Test
    void getById() {
        UUID accountId = accounts.get(0).getId();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accounts.get(0)));
        Account result = accountService.getById(accountId);
        assertNotNull(result);
        assertEquals(accountId, result.getId());
        assertEquals("Savings Accounts", result.getName());
        assertEquals(3400.0, result.getBalance());
    }

    @Test
    void getByIdWhenAccountNotFound() {
        UUID accountId = accounts.get(0).getId();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.getById(accountId));
    }

    @Test
    void getByStatus() {
        AccountStatus status = AccountStatus.ACTIVE;
        Mockito.when(accountRepository.findAllByStatus(status)).thenReturn(Collections.singletonList(accounts.get(0)));
        List<Account> result = accountService.getByStatus(status);
        assertNotNull(result);
        assertEquals(status, result.get(0).getStatus());
    }

    @Test
    void getByStatusWhenAccountsNotFound() {
        AccountStatus status = AccountStatus.ACTIVE;
        Mockito.when(accountRepository.findAllByStatus(status)).thenReturn(new ArrayList<>());
        assertThrows(AccountNotFoundException.class, () -> accountService.getByStatus(status));
    }

    @Test
    void getByClientId() {
        UUID clientId = clients.get(0).getId();
        List<Account> clientAccounts = accounts.stream()
                .filter(account -> clientId.equals(account.getClient().getId()))
                .toList();
        Mockito.when(accountRepository.findAllByClientId(clientId)).thenReturn(clientAccounts);
        List<Account> result = accountService.getByClientId(clientId);
        assertNotNull(result);
        assertEquals(clientAccounts.size(), result.size());
        assertEquals(clientId, result.get(0).getClient().getId());
    }

    @Test
    void getByClientIdWhenAccountsNotFound() {
        UUID clientId = clients.get(0).getId();
        Mockito.when(accountRepository.findAllByClientId(clientId)).thenReturn(new ArrayList<>());
        assertThrows(AccountNotFoundException.class, () -> accountService.getByClientId(clientId));
    }

    @Test
    void balance() {
        UUID accountId = accounts.get(0).getId();
        double expected = accounts.get(0).getBalance();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(accounts.get(0)));
        double result = accountService.balance(accountId);
        assertEquals(expected, result);
    }

    @Test
    void create() {
        UUID clientId = clients.get(0).getId();
        Account newAccount = new Account(UUID.randomUUID(), "Test Accounts", AccountType.DEPOSIT, AccountStatus.ACTIVE, 12000.0, CurrencyCode.USD, new Timestamp(System.currentTimeMillis()), null, null, clients.get(0), new ArrayList<>(), new ArrayList<>());
        Mockito.when(accountRepository.save(newAccount)).thenReturn(newAccount);
        Account result = accountService.create(newAccount, clientId);
        assertNotNull(result);
        assertEquals(newAccount.getId(), result.getId());
        assertEquals(newAccount.getBalance(), result.getBalance());
        assertEquals(newAccount.getCurrencyCode(), result.getCurrencyCode());
    }

    @Test
    void deposit() {
        UUID accountId = accounts.get(0).getId();
        double balance = accounts.get(0).getBalance();
        double amount = 1000.0;
        String description = "Deposit 1000 USD";

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(accounts.get(0)));
        accountService.deposit(accountId, amount, description);

        double expectedBalance = balance + amount;
        assertEquals(expectedBalance, accounts.get(0).getBalance());
    }

    @Test
    void withdraw() {
        UUID accountId = accounts.get(0).getId();
        double balance = accounts.get(0).getBalance();
        double amount = 1000.0;
        String description = "Withdraw 1000 USD";

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(accounts.get(0)));
        accountService.withdraw(accountId, amount, description);

        double expectedBalance = balance - amount;
        assertEquals(expectedBalance, accounts.get(0).getBalance());
    }

    @Test
    void transfer() {
        UUID senderId = accounts.get(0).getId();
        UUID receiverId = accounts.get(1).getId();
        double senderBalance = accounts.get(0).getBalance();
        double receiverBalance = accounts.get(1).getBalance();
        double amount = 1000.0;
        String description = "Transfer 1000 USD";

        double exchangeRate = CurrencyConverter.getExchangeRate(accounts.get(0).getCurrencyCode(), accounts.get(1).getCurrencyCode());
        double equivalentAmount = amount * exchangeRate;

        Mockito.when(accountRepository.findById(senderId)).thenReturn(Optional.of(accounts.get(0)));
        Mockito.when(accountRepository.findById(receiverId)).thenReturn(Optional.of(accounts.get(1)));

        accountService.transfer(senderId, receiverId, amount, description);

        double senderExpectedBalance = senderBalance - amount;
        double receiverExpectedBalance = receiverBalance + equivalentAmount;

        assertEquals(senderExpectedBalance, accounts.get(0).getBalance());
        assertEquals(receiverExpectedBalance, accounts.get(1).getBalance());

    }

    @Test
    void getTransactionHistory() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction(TransactionType.DEPOSIT, 1000.0, "Deposit 1000 USD", accounts.get(0), null),
                new Transaction(TransactionType.PAYMENT, 1000.0, "Payment 1000 USD", null, accounts.get(0)),
                new Transaction(TransactionType.WITHDRAWAL, 1000.0, "Withdrawal 1000 USD", accounts.get(0), accounts.get(1)));

        Mockito.when(transactionService.findByAccountId(accounts.get(0).getId())).thenReturn(transactions);

        List<Transaction> result = accountService.getTransactionHistory(accounts.get(0).getId());
        assertNotNull(result);
        assertEquals(result.size(), transactions.size());
    }

    @Test
    void deleteById() {
        UUID accountId = accounts.get(0).getId();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(accounts.get(0)));
        accountService.deleteById(accountId);
        Mockito.verify(accountRepository).deleteById(accountId);
    }

    @Test
    void deleteByIdAccountNotFound() {
        UUID accountId = UUID.randomUUID();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.deleteById(accountId));
    }
}