package org.example.com.service;


import lombok.RequiredArgsConstructor;
import org.example.com.controller.ManagerController;
import org.example.com.entity.Account;
import org.example.com.entity.Transaction;
import org.example.com.entity.enums.CurrencyCode;
import org.example.com.entity.enums.TransactionType;
import org.example.com.exception.AccountNotFoundException;
import org.example.com.exception.InsufficientBalanceException;
import org.example.com.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account getById(UUID id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            throw new AccountNotFoundException(String.format("Account with id %s not found", id));
        }
        return account;
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
    @Transactional
    public Account deposit(UUID id, Double amount, String description) {
        Account account = getById(id);
        if (account == null) {
            throw new AccountNotFoundException(String.format("Account with id %s not found", id));
        }
        account.setBalance(account.getBalance() + amount);

        Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount, description, account, null);
        transactionService.create(transaction);

        return account;
    }

    @Override
    @Transactional
    public Account withdraw(UUID id, Double amount, String description) {
        Account account = getById(id);
        if (account == null) {
            throw new AccountNotFoundException(String.format("Account with id %s not found", id));
        }
        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);

        Transaction transaction = new Transaction(TransactionType.WITHDRAWAL, amount, description, null, account);

        transactionService.create(transaction);

        return account;
    }

    @Override
    @Transactional
    public Transaction transfer(UUID senderId, UUID receiverId, Double amount, String description) {
        Account senderAccount = getById(senderId);
        Account receiverAccount = getById(receiverId);
        if (senderAccount == null || receiverAccount == null) {
            throw new AccountNotFoundException("Account not found");
        }
        if (senderAccount.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        Double exchangeRate = getExchangeRate(senderAccount.getCurrencyCode(), receiverAccount.getCurrencyCode());
        Double equivalentAmount = amount * exchangeRate;
        senderAccount.setBalance(senderAccount.getBalance() - amount);
        receiverAccount.setBalance(receiverAccount.getBalance() + equivalentAmount);

        Transaction transaction = new Transaction(TransactionType.TRANSFER, amount, description, senderAccount, receiverAccount);
        transactionService.create(transaction);

        return transaction;
    }


    @Override
    public List<Transaction> getTransactionHistory(UUID id) {
        return transactionService.findByAccountId(id);
    }

    @Override
    public void delete(Account account) {
        accountRepository.delete(account);
    }

    @Override
    public void deleteById(UUID id) {
        logger.info("УДАЛЕНИЕЕЕЕЕЕЕ ID: {}", id);
        accountRepository.deleteById(id);
        logger.info("УЛАЛЕННННННН account ID: {}", id);

    }

    private Double getExchangeRate(CurrencyCode fromCurrency, CurrencyCode toCurrency) {
        double usdToEurRate = 0.92;
        double usdToGbpRate = 0.78;
        double eurToUsdRate = 1.09;
        double eurToGbpRate = 0.85;
        double gbpToUsdRate = 1.28;
        double gbpToEurRate = 1.17;
        if (fromCurrency.equals(CurrencyCode.USD) && toCurrency.equals(CurrencyCode.EUR)) {
            return usdToEurRate;
        } else if (fromCurrency.equals(CurrencyCode.USD) && toCurrency.equals(CurrencyCode.GBP)) {
            return usdToGbpRate;
        } else if (fromCurrency.equals(CurrencyCode.EUR) && toCurrency.equals(CurrencyCode.USD)) {
            return eurToUsdRate;
        } else if (fromCurrency.equals(CurrencyCode.EUR) && toCurrency.equals(CurrencyCode.GBP)) {
            return eurToGbpRate;
        } else if (fromCurrency.equals(CurrencyCode.GBP) && toCurrency.equals(CurrencyCode.USD)) {
            return gbpToUsdRate;
        } else if (fromCurrency.equals(CurrencyCode.GBP) && toCurrency.equals(CurrencyCode.EUR)) {
            return gbpToEurRate;
        }
        return 1.0;
    }
}
