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
        return repository.findAll();
    }

    @Override
    public Transaction getById(UUID id) {
        Transaction transaction = repository.getReferenceById(id);
        if (transaction == null) {
            throw new TransactionNotFoundException(String.format("Transaction with id %s not found", id));
        }
        return transaction;
    }

    @Override
    public Transaction create(Transaction transaction) {
        return repository.save(transaction);
    }

    @Override
    public List<Transaction> findByAccountId(UUID accountId) {
        return repository.findByAccountDebitIdOrAccountCreditId(accountId, accountId);
    }
}
