package org.example.com.service;


import lombok.RequiredArgsConstructor;
import org.example.com.converter.Converter;
import org.example.com.dto.AccountDto;
import org.example.com.dto.TransactionDto;
import org.example.com.entity.Account;
import org.example.com.entity.Client;
import org.example.com.entity.Transaction;
import org.example.com.entity.enums.AccountStatus;
import org.example.com.entity.enums.CurrencyCode;
import org.example.com.entity.enums.TransactionType;
import org.example.com.exception.AccountNotFoundException;
import org.example.com.exception.ClientNotFoundException;
import org.example.com.exception.InsufficientBalanceException;
import org.example.com.repository.AccountRepository;
import org.example.com.repository.ClientRepository;
import org.example.com.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;
    private final Converter<Account, AccountDto> accountDtoConverter;
    private final Converter<Transaction, TransactionDto> transactionDtoConverter;


    @Override
    @Transactional
    public List<AccountDto> getAll() {
        return accountRepository.findAll().stream()
                .map(accountDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto getById(UUID id) {
        AccountDto accountDto = accountDtoConverter.toDto(accountRepository.findById(id).orElse(null));
        if (accountDto == null) {
            throw new AccountNotFoundException(String.format("Account with id %s not found", id));
        }
        return accountDto;
    }

    @Override
    public List<AccountDto> getByStatus(AccountStatus accountStatus) {
        return accountRepository.findAllByStatus(accountStatus).stream()
                .map(accountDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountDto> getByClientId(UUID clientId) {
        return accountRepository.findAllByClientId(clientId).stream()
                .map(accountDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Double balance(UUID id) {
        return getById(id).getBalance();
    }

    @Override
    public AccountDto create(AccountDto accountDto, UUID clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + clientId));
        Account account = accountDtoConverter.toEntity(accountDto);
        account.setClient(client);
        Account createdAccount = accountRepository.save(account);
        return accountDtoConverter.toDto(createdAccount);
    }

    @Override
    public void deposit(UUID accountId, Double amount, String description) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (amount <= 0) {
            throw new InsufficientBalanceException("Amount must be greater than zero");
        }

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount, description, account, null);
        transactionRepository.save(transaction);
    }

    @Override
    public void withdraw(UUID accountId, Double amount, String description) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account with id %s not found", accountId)));

        if (amount <= 0) {
            throw new InsufficientBalanceException("Amount must be greater than zero");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount, description, null, account);
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
   public void transfer(UUID senderId, UUID receiverId, Double amount, String description) {
        Account senderAccount = accountRepository.findById(senderId)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account with id %s not found", senderId)));
        Account receiverAccount = accountRepository.findById(receiverId)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account with id %s not found", receiverId)));

        if (senderAccount.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        Double exchangeRate = getExchangeRate(senderAccount.getCurrencyCode(), receiverAccount.getCurrencyCode());
        Double equivalentAmount = amount * exchangeRate;
        senderAccount.setBalance(senderAccount.getBalance() - amount);
        receiverAccount.setBalance(receiverAccount.getBalance() + equivalentAmount);

        Transaction transaction = new Transaction(TransactionType.TRANSFER, amount, description, senderAccount, receiverAccount);
        transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionDto> getTransactionHistory(UUID id) {
        return transactionService.findByAccountId(id);
    }

    @Transactional
    @Override
    public void deleteById(UUID id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            UUID clientId = account.getClient().getId();

            transactionRepository.deleteByAccountCreditOrAccountDebit(account, account);
            accountRepository.deleteById(id);
        }
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
