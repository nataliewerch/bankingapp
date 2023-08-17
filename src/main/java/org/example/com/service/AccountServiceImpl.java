package org.example.com.service;


import lombok.RequiredArgsConstructor;
import org.example.com.entity.Account;
import org.example.com.entity.Transaction;
import org.example.com.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account getById(UUID id) {
        return accountRepository.getReferenceById(id);
    }

    @Override
    public Double balance(UUID id) {
        return getById(id).getBalance();
    }

    @Override
    public Account create(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account deposit(UUID id, double amount, String description) {
        Account account = getById(id);
        if (account != null) {
            account.setBalance(account.getBalance() + amount);

            Transaction transaction = new Transaction();
            transaction.setType("deposit");
            transaction.setAmount(amount);
            transaction.setDescription(description);
            transaction.setAccountDebit(account);
            transaction.setAccountCredit(account);
            transactionService.createTransaction(transaction);
        }
        return accountRepository.save(account);
    }

    @Override
    public Account withdraw(UUID id, double amount, String description) {
        Account account = getById(id);
        if (account != null) {
            account.setBalance(account.getBalance() - amount);

            Transaction transaction = new Transaction();
            transaction.setType("credit");
            transaction.setAmount(amount);
            transaction.setDescription(description);
            transaction.setAccountDebit(account);
            transaction.setAccountCredit(account);

            transactionService.createTransaction(transaction);
        }
        return accountRepository.save(account);
    }

    @Override
    public List<Transaction> getTransactionHistory(UUID id) {
        return transactionService.findByAccountId(id);
    }

    @Override
    public void delete(Account account) {
        accountRepository.delete(account);
    }

    public void deleteById(UUID id) {
        accountRepository.deleteById(id);
    }
}
