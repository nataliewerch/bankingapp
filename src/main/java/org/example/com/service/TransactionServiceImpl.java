package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Transaction;
import org.example.com.exception.TransactionNotFoundException;
import org.example.com.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    @Override
    public List<Transaction> getAll() {
        List<Transaction> transactions = repository.findAll();
        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException("No transactions found");
        }
        return transactions;
    }

    @Override
    public Transaction getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(String.format("Transaction with id %s not found", id)));
    }

    @Override
    public Transaction create(Transaction transaction) {
        return repository.save(transaction);
    }

    @Override
    public List<Transaction> findByAccountId(UUID accountId) {
        List<Transaction> transactions = repository.findByAccountDebitIdOrAccountCreditId(accountId, accountId);
        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException("No transactions found");
        }
        return transactions;
    }
}
