package org.example.com.service;

import org.example.com.entity.Account;
import org.example.com.entity.Transaction;
import org.example.com.entity.enums.AccountStatus;
import org.example.com.entity.enums.AccountType;
import org.example.com.entity.enums.CurrencyCode;
import org.example.com.entity.enums.TransactionType;
import org.example.com.exception.TransactionNotFoundException;
import org.example.com.repository.TransactionRepository;
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
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private List<Transaction> transactions;
    private List<Account> accounts;


    @BeforeEach
    void init() {
        accounts = Arrays.asList(
                new Account(UUID.randomUUID(), "Savings Accounts", AccountType.DEPOSIT, AccountStatus.ACTIVE, 3400.0, CurrencyCode.USD, null, null, null, null, null, null),
                new Account(UUID.randomUUID(), "Credit Card", AccountType.CREDIT, AccountStatus.ACTIVE, 25000.0, CurrencyCode.EUR, null, null, null, null, null, null),
                new Account(UUID.randomUUID(), "Debit Card", AccountType.DEPOSIT, AccountStatus.ACTIVE, 3400.0, CurrencyCode.EUR, null, null, null, null, null, null));

        transactions = Arrays.asList(
                new Transaction(UUID.randomUUID(), TransactionType.DEPOSIT, 200.0, "Deposit to Account", null, accounts.get(0), null),
                new Transaction(UUID.randomUUID(), TransactionType.PAYMENT, 500.0, "Payment", null, null, accounts.get(1)),
                new Transaction(UUID.randomUUID(), TransactionType.WITHDRAWAL, 700.0, "Withdrawal", null, accounts.get(1), accounts.get(2)));
    }

    @Test
    void getAll() {
        Mockito.when(transactionRepository.findAll()).thenReturn(transactions);
        assertEquals(transactions.size(), transactionService.getAll().size());
    }

    @Test
    void getAllWithNoTransactions() {
        Mockito.when(transactionRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getAll());
    }

    @Test
    void getById() {
        UUID transactionId = transactions.get(0).getId();
        Mockito.when(transactionRepository.findById(transactionId)).thenReturn(Optional.ofNullable(transactions.get(0)));
        Transaction result = transactionService.getById(transactionId);
        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        assertEquals(transactions.get(0).getId(), result.getId());
        assertEquals(transactions.get(0).getAmount(), result.getAmount());
        assertEquals(transactions.get(0).getType(), result.getType());
        assertEquals(transactions.get(0).getDescription(), result.getDescription());
    }

    @Test
    void getByIdWhenTransactionNotFound() {
        Mockito.when(transactionRepository.findById(transactions.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getById(transactions.get(0).getId()));
    }

    @Test
    void create() {
        Mockito.when(transactionRepository.save(transactions.get(0))).thenReturn(transactions.get(0));
        Transaction result = transactionService.create(transactions.get(0));
        assertNotNull(result);
        assertEquals(transactions.get(0).getId(), result.getId());
        assertEquals(transactions.get(0).getAmount(), result.getAmount());
        assertEquals(transactions.get(0).getType(), result.getType());
        assertEquals(transactions.get(0).getDescription(), result.getDescription());
    }

    @Test
    void findByAccountId() {
        Mockito.when(transactionRepository.findByAccountDebitIdOrAccountCreditId(accounts.get(0).getId(), accounts.get(0).getId())).thenReturn(transactions);
        List<Transaction> result = transactionService.findByAccountId(accounts.get(0).getId());
        assertNotNull(result);
        assertEquals(transactions, result);
    }

    @Test
    void findByAccountIdWhenNoTransactions() {
        Mockito.when(transactionRepository.findByAccountDebitIdOrAccountCreditId(accounts.get(0).getId(), accounts.get(0).getId())).thenReturn(new ArrayList<>());
        assertThrows(TransactionNotFoundException.class, () -> transactionService.findByAccountId(accounts.get(0).getId()));
    }

    @Test
    void deleteById() {
        Mockito.when(transactionRepository.findById(transactions.get(0).getId())).thenReturn(Optional.of(transactions.get(0)));
        transactionService.delete(transactions.get(0).getId());
        Mockito.verify(transactionRepository).deleteById(transactions.get(0).getId());
    }

    @Test
    void deleteByIdWhenTransactionNotFound() {
        Mockito.when(transactionRepository.findById(transactions.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> transactionService.delete(transactions.get(0).getId()));
    }
}