package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Transaction;
import org.example.com.exception.TransactionNotFoundException;
import org.example.com.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implementation of the TransactionService interface for managing transaction-related operations.
 *
 * @author Natalie Werch
 */
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    /**
     * Retrieves a list of all transactions.
     *
     * @return A list of Transaction objects.
     * @throws TransactionNotFoundException If no transactions are found in the database.
     */
    @Override
    public List<Transaction> getAll() {
        List<Transaction> transactions = repository.findAll();
        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException("No transactions found");
        }
        return transactions;
    }

    /**
     * Retrieves a transaction by its unique identifier.
     *
     * @param id The unique identifier of the transaction.
     * @return The Transaction object with the specified ID.
     * @throws TransactionNotFoundException If a transaction with the specified ID is not found in the database.
     */
    @Override
    public Transaction getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(String.format("Transaction with id %s not found", id)));
    }

    /**
     * Creates a new transaction.
     *
     * @param transaction The Transaction object to be created.
     * @return The created Transaction object.
     */
    @Override
    public Transaction create(Transaction transaction) {
        return repository.save(transaction);
    }

    /**
     * Retrieves a list of transactions associated with a specific account.
     *
     * @param accountId The unique identifier of the account.
     * @return A list of Transaction objects associated with the specified account.
     * @throws TransactionNotFoundException If no transactions are found for the specified account.
     */
    @Override
    public List<Transaction> findByAccountId(UUID accountId) {
        List<Transaction> transactions = repository.findByAccountDebitIdOrAccountCreditId(accountId, accountId);
        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException("No transactions found");
        }
        return transactions;
    }

    /**
     * Deletes a transaction by its unique identifier.
     *
     * @param id The unique identifier of the transaction to be deleted.
     * @throws TransactionNotFoundException If a transaction with the specified ID is not found in the database.
     */
    @Override
    public void delete(UUID id) {
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(String.format("Transaction with id %s not found", id)));
        repository.deleteById(id);
    }
}
