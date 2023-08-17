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
    public Transaction createTransaction(Transaction transaction) {
        return repository.save(transaction);
    }

    @Override
    public List<Transaction> findByAccountId(UUID accountId) {
        List<Transaction> debitTransaction = repository.findAllByAccountDebitId(accountId);
        List<Transaction> creditTransaction = repository.findAllByAccountCreditId(accountId);

        Set<Transaction> allTransaction = new HashSet<>();
        allTransaction.addAll(debitTransaction);
        allTransaction.addAll(creditTransaction);

        return new ArrayList<>(allTransaction);
    }
}
