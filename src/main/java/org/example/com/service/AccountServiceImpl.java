package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.*;
import org.example.com.entity.enums.AccountStatus;
import org.example.com.entity.enums.TransactionType;
import org.example.com.exception.AccountNotFoundException;
import org.example.com.exception.ClientNotFoundException;
import org.example.com.exception.InsufficientBalanceException;
import org.example.com.exception.InvalidAmountException;
import org.example.com.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the AccountService interface for managing account-related operations.
 *
 * @author Natalie Werch
 */
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Get a list of all accounts.
     *
     * @return A list of all accounts.
     * @throws AccountNotFoundException If no accounts are found.
     */
    @Override
    public List<Account> getAll() {
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            throw new AccountNotFoundException("No accounts found");
        }
        return accounts;
    }

    /**
     * Get an account by its unique identifier.
     *
     * @param id The unique identifier of the account.
     * @return The account with the specified ID.
     * @throws AccountNotFoundException If no account is found with the specified ID.
     */
    @Override
    public Account getById(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account with id %s not found", id)));
    }

    /**
     * Get a list of accounts by their status.
     *
     * @param accountStatus The status of the accounts to retrieve.
     * @return A list of accounts with the specified status.
     * @throws AccountNotFoundException If no accounts are found with the specified status.
     */
    @Override
    public List<Account> getByStatus(AccountStatus accountStatus) {
        List<Account> accounts = accountRepository.findAllByStatus(accountStatus);
        if (accounts.isEmpty()) {
            throw new AccountNotFoundException(String.format("Account with status %s not found", accountStatus));
        }
        return accounts;
    }

    /**
     * Get a list of accounts by the unique identifier of their client.
     *
     * @param clientId The unique identifier of the client.
     * @return A list of accounts associated with the specified client.
     * @throws AccountNotFoundException If no accounts are found for the specified client ID.
     */
    @Override
    public List<Account> getByClientId(UUID clientId) {
        List<Account> accounts = accountRepository.findAllByClientId(clientId);
        if (accounts.isEmpty()) {
            throw new AccountNotFoundException(String.format("Account with clients id %s not found", clientId));
        }
        return accounts;
    }

    /**
     * Get the balance of an account by its unique identifier.
     *
     * @param id The unique identifier of the account.
     * @return The balance of the account.
     */
    @Override
    @Transactional
    public Double balance(UUID id) {
        return getById(id).getBalance();
    }

    /**
     * Create a new account associated with a client.
     *
     * @param account  The account entity to create.
     * @param clientId The unique identifier of the client to associate with the account.
     * @return The newly created account.
     * @throws ClientNotFoundException If the client with the specified ID is not found.
     */
    @Override
    @Transactional
    public Account create(Account account, UUID clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(String.format("Client not found with id %s: ", clientId)));
        account.setClient(client);
        return accountRepository.save(account);
    }

    /**
     * Deposit a specified amount into an account.
     *
     * @param accountId   The unique identifier of the account to deposit into.
     * @param amount      The amount to deposit into the account.
     * @param description A description of the deposit.
     * @throws AccountNotFoundException If the account with the specified ID is not found.
     * @throws InvalidAmountException   If the deposit amount is not greater than zero.
     */
    @Override
    @Transactional
    public void deposit(UUID accountId, Double amount, String description) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount, description, account, null);
        transactionRepository.save(transaction);
    }

    /**
     * Withdraw a specified amount from an account.
     *
     * @param accountId   The unique identifier of the account to withdraw from.
     * @param amount      The amount to withdraw from the account.
     * @param description A description of the withdrawal.
     * @throws AccountNotFoundException     If the account with the specified ID is not found.
     * @throws InvalidAmountException       If the withdrawal amount is not greater than zero.
     * @throws InsufficientBalanceException If the account balance is insufficient for the withdrawal.
     */
    @Override
    @Transactional
    public void withdraw(UUID accountId, Double amount, String description) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account with id %s not found", accountId)));

        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        if ((account.getBalance() - amount) < 0) {
            throw new InsufficientBalanceException("Insufficient funds in the account");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction(TransactionType.WITHDRAWAL, amount, description, null, account);
        transactionRepository.save(transaction);
    }

    /**
     * Transfer a specified amount between two accounts.
     *
     * @param senderId    The unique identifier of the sender's account.
     * @param receiverId  The unique identifier of the receiver's account.
     * @param amount      The amount to transfer between accounts.
     * @param description A description of the transfer.
     * @throws AccountNotFoundException     If the sender or receiver account with the specified ID is not found.
     * @throws InsufficientBalanceException If the sender account balance is insufficient for the transfer.
     */
    @Override
    @Transactional
    public void transfer(UUID senderId, UUID receiverId, Double amount, String description) {
        Account senderAccount = accountRepository.findById(senderId)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account with id %s not found", senderId)));
        Account receiverAccount = accountRepository.findById(receiverId)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account with id %s not found", receiverId)));

        if (senderAccount.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient funds in the account");
        }

        Double exchangeRate = CurrencyConverter.getExchangeRate(senderAccount.getCurrencyCode(), receiverAccount.getCurrencyCode());
        Double equivalentAmount = amount * exchangeRate;
        senderAccount.setBalance(senderAccount.getBalance() - amount);
        receiverAccount.setBalance(receiverAccount.getBalance() + equivalentAmount);

        Transaction transaction = new Transaction(TransactionType.TRANSFER, amount, description, senderAccount, receiverAccount);
        transactionRepository.save(transaction);
    }

    /**
     * Retrieve the transaction history for a specific account based on its unique identifier.
     *
     * @param id The unique identifier of the account for which the transaction history is requested.
     * @return A list of transactions associated with the specified account.
     */
    @Override
    @Transactional
    public List<Transaction> getTransactionHistory(UUID id) {
        return transactionService.findByAccountId(id);
    }

    /**
     * Delete an account by its unique identifier.
     *
     * @param id The unique identifier of the account to delete.
     * @throws AccountNotFoundException If the account with the specified ID is not found.
     */
    @Override
    public void deleteById(UUID id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            accountRepository.deleteById(id);
        } else {
            throw new AccountNotFoundException(String.format("Account with clients id %s not found", id));
        }
    }
}
