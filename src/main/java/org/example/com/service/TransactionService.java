package org.example.com.service;

import org.example.com.entity.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    Transaction createTransaction(Transaction transaction);

    List<Transaction> findByAccountId(UUID accountId);
}
