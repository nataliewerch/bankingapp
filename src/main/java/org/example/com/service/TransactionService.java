package org.example.com.service;

import org.example.com.entity.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    List<Transaction> getAll();
    Transaction getById(UUID id);
    Transaction create(Transaction transaction);

    List<Transaction> findByAccountId(UUID accountId);




}
