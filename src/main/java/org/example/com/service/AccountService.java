package org.example.com.service;

import org.example.com.dto.AccountDto;
import org.example.com.dto.TransactionDto;;
import org.example.com.entity.enums.AccountStatus;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<AccountDto> getAll();

    AccountDto getById(UUID id);

    List<AccountDto> getByStatus(AccountStatus accountStatus);

    List<AccountDto> getByClientId(UUID clientId);

    Double balance(UUID id);

    AccountDto create(AccountDto accountDto, UUID clientId);

    void deposit(UUID accountId, Double amount, String description);


    void withdraw(UUID id, Double amount, String description);

    void transfer(UUID senderId, UUID receiverId, Double amount, String description);

    List<TransactionDto> getTransactionHistory(UUID id);

    void deleteById(UUID id);
}
