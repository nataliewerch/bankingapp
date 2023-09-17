package org.example.com.service;

import org.example.com.entity.Account;
import org.example.com.entity.Transaction;
import org.example.com.entity.enums.AccountStatus;

import java.util.List;
import java.util.UUID;

/**
 * This interface defines the contract for managing user accounts and transactions.
 *
 * @author Natalie Werch
 */
public interface AccountService {
    List<Account> getAll();

    Account getById(UUID id);

    List<Account> getByStatus(AccountStatus accountStatus);

    List<Account> getByClientId(UUID clientId);

    Double balance(UUID id);

    Account create(Account account, UUID clientId);

    void deposit(UUID accountId, Double amount, String description);

    void withdraw(UUID id, Double amount, String description);

    void transfer(UUID senderId, UUID receiverId, Double amount, String description);

    List<Transaction> getTransactionHistory(UUID id);

    void deleteById(UUID id);
}
