package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.*;
import org.example.com.entity.enums.AccountStatus;
import org.example.com.entity.enums.TransactionType;
import org.example.com.exception.*;
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
    private final ClientService clientService;

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

    @Override
    @Transactional
    public Double balance(UUID id) {
        return getById(id).getBalance();
    }

    /**
     * Retrieves the balance of a client's account.
     *
     * @param clientId  The unique identifier of the client.
     * @param accountId The unique identifier of the client's account.
     * @return The balance of the client's account.
     * @throws AccountNotFoundException If the specified account is not found for the client.
     */
    @Override
    public Double balance(UUID clientId, UUID accountId) {
        Account account = accountRepository.findByIdAndClientId(accountId, clientId)
                .orElseThrow(() -> new AccountNotFoundException(
                        String.format("Account with id: %s not found for the client with id: %s ", accountId, clientId)));
        return account.getBalance();
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
        Client client = clientService.getById(clientId);
        account.setClient(client);
        return accountRepository.save(account);
    }

    /**
     * Deposit a specified amount into an account.
     *
     * @param accountId   The unique identifier of the account to deposit into.
     * @param amount      The amount to deposit into the account.
     * @param description A description of the deposit.
     * @throws AccountInactiveException If the account is not active.
     * @throws InvalidAmountException   If the deposit amount is not greater than zero.
     */
    @Override
    @Transactional
    public void deposit(UUID accountId, Double amount, String description) {
        Account account = getById(accountId);

        if (!isActive(accountId)) {
            throw new AccountInactiveException(String.format("Account with id %s is not active:", accountId));
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        account.setBalance(account.getBalance() + amount);

        Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount, description, account, null);
        transactionService.create(transaction);
    }

    /**
     * Withdraw a specified amount from an account.
     *
     * @param accountId   The unique identifier of the account to withdraw from.
     * @param amount      The amount to withdraw from the account.
     * @param description A description of the withdrawal.
     * @throws AccountInactiveException     If the account is not active.
     * @throws InvalidAmountException       If the withdrawal amount is not greater than zero.
     * @throws InsufficientBalanceException If the account balance is insufficient for the withdrawal.
     */
    @Override
    @Transactional
    public void withdraw(UUID accountId, Double amount, String description) {
        Account account = getById(accountId);

        if (!isActive(accountId)) {
            throw new AccountInactiveException(String.format("Account with id %s is not active:", accountId));
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        if ((account.getBalance() - amount) < 0) {
            throw new InsufficientBalanceException("Insufficient funds in the account");
        }

        account.setBalance(account.getBalance() - amount);

        Transaction transaction = new Transaction(TransactionType.WITHDRAWAL, amount, description, null, account);
        transactionService.create(transaction);
    }

    /**
     * Transfer a specified amount between two accounts.
     *
     * @param senderId    The unique identifier of the sender's account.
     * @param receiverId  The unique identifier of the receiver's account.
     * @param amount      The amount to transfer between accounts.
     * @param description A description of the transfer.
     * @throws AccountInactiveException     If the sender or receiver account is not active.
     * @throws InsufficientBalanceException If the sender account balance is insufficient for the transfer.
     */
    @Override
    @Transactional
    public void transfer(UUID senderId, UUID receiverId, Double amount, String description) {
        Account senderAccount = getById(senderId);
        Account receiverAccount = getById(receiverId);

        if (!isActive(senderId)) {
            throw new AccountInactiveException(String.format("Sender account with id %s is not active:", senderId));
        }
        if (!isActive(receiverId)) {
            throw new AccountInactiveException(String.format("Receiver account with id %s is not active:", receiverId));
        }
        if (senderAccount.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient funds in the account");
        }

        Double exchangeRate = CurrencyConverter.getExchangeRate(senderAccount.getCurrencyCode(), receiverAccount.getCurrencyCode());
        Double equivalentAmount = amount * exchangeRate;
        senderAccount.setBalance(senderAccount.getBalance() - amount);
        receiverAccount.setBalance(receiverAccount.getBalance() + equivalentAmount);

        Transaction transaction = new Transaction(TransactionType.TRANSFER, amount, description, senderAccount, receiverAccount);
        transactionService.create(transaction);
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

    /**
     * Changes the status of an account.
     *
     * @param id        The unique identifier of the account.
     * @param newStatus The new status to set for the account.
     * @return The updated Client object with the new status.
     */
    @Override
    public Account changeStatus(UUID id, AccountStatus newStatus) {
        Account account = getById(id);

        if (account.getStatus() != newStatus) {
            account.setStatus(newStatus);
            accountRepository.save(account);
        }
        return account;
    }

    /**
     * Checks if an account associated with the given UUID is active.
     *
     * @param id The UUID of the account to check.
     * @return true if the account is active, false otherwise.
     */
    @Override
    public boolean isActive(UUID id) {
        Account account = getById(id);
        return account != null && account.getStatus() == AccountStatus.ACTIVE;
    }
}
