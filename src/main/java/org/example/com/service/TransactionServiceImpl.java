package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Transaction;
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
        return repository.getReferenceById(id);
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
