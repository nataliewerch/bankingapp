package org.example.com.service;

import org.example.com.entity.Account;
import org.example.com.entity.Transaction;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<Account> getAll();

    Account getById(UUID id);

    Double balance(UUID id);

    Account create(Account account);

    Account deposit(UUID id, double amount, String description);

    Account withdraw(UUID id, double amount, String description);

    List<Transaction> getTransactionHistory(UUID id);

    void delete(Account account);

    void deleteById(UUID id);
}
